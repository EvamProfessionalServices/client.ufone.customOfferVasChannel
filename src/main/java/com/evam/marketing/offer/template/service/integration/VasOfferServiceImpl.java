package com.evam.marketing.offer.template.service.integration;

import com.evam.marketing.offer.template.configuration.VasConfig;
import com.evam.marketing.offer.template.service.event.KafkaProducerServiceImpl;
import com.evam.marketing.offer.template.service.event.model.AbstractOfferResponseEvent;
import com.evam.marketing.offer.template.service.stream.model.request.BoltOnActionRequest;
import com.evam.marketing.offer.template.service.stream.model.request.BoltOnProfileRequest;
import com.evam.marketing.offer.template.service.stream.model.request.BoltOnRequest;
import com.evam.marketing.offer.template.service.stream.model.request.BoltOnVerificationRequest;
import com.evam.marketing.offer.template.service.stream.model.request.CustomBenefitRequest;
import com.evam.marketing.offer.template.service.stream.model.request.StreamRequest;
import com.evam.marketing.offer.template.utils.VasOfferUtil;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * The {@code VasOfferServiceImpl} class
 *
 * @author Abdul Wadood
 * @since 8.0.2
 */
@Service
@Log4j2
public class VasOfferServiceImpl extends AbstractOfferService {
    private static final String ERROR = "failed";
    private static final String N = "N";
    private static final String PHASE_PROFILE = "profile";
    private static final String PHASE_VERIFY = "verify";
    private static final String PHASE_ACTION = "action";

    @Value("${custom-offer-event-name.fail}")
    private String eventNameFail;
    @Value("${custom-offer-event-name.success}")
    private String eventNameSuccess;

    @Autowired
    private VasConfig config;
    @Autowired
    private PerformanceCounter performance;
    @Autowired
    private SoapActionService soapActionService;
    @Autowired
    private SoapProfileService soapProfileService;
    @Autowired
    private SoapVerifyService soapVerifyService;
    @Autowired
    private PersistenceActionService persistenceActionService;
    @Autowired
    private PersistenceProfileService persistenceProfileService;
    @Autowired
    private PersistenceVerifyService persistenceVerifyService;

    public VasOfferServiceImpl(KafkaProducerServiceImpl kafkaProducerService) {
        super(kafkaProducerService);
    }

    @Override
    @RateLimiter(name = "client-limiter")
    public void execute(StreamRequest streamRequest) {
        log.debug("request received: {}", streamRequest);
        BoltOnRequest request = this.buildBoltOnRequest(this.config.getPhase(), (CustomBenefitRequest) streamRequest);
        request.updateRequestParams();

        String message = "success";
        AbstractOfferResponseEvent event = null;
        try {
            if (!request.getSilentMode().equals(N)) {
                message = "Silent Mode";
                this.performance.incrementEventCountSilent();
            } else if (this.config.isTimeRestricted() && !VasOfferUtil.isTimeWindowOk(
                    this.config.getSilentModeStartTime(), this.config.getSilentModeEndTime()
            )) {
                message = "Time Constraint";
                this.performance.incrementEventCountSilent();
            } else {
                this.invokeBoltOnService(this.config.getPhase(), request);
                this.performance.incrementEventCountSuccess();
            }

            event = this.toCustomOfferSuccessEvent(this.eventNameSuccess, request.getCustomBenefitRequest(), message);
        } catch (IllegalAccessError ex) {
            log.error("probably phase configuration is incorrect.", ex);
        } catch (Throwable e) {
            log.error("an unexpected error occurred for request {}", request, e);
            event = this.toCustomOfferFailEvent(this.eventNameFail, request.getCustomBenefitRequest(), ERROR, e.getMessage());
            this.performance.incrementEventCountFail();
        }

        if (event != null) {
            this.updateBoltOnEventParams(this.config.getPhase(), event, request);
            this.sendEvent(event);
        }

        if (this.config.isWriteToDb()) {
            this.persist(this.config.getPhase(), request);
        }
    }

    private BoltOnRequest buildBoltOnRequest(String phase, CustomBenefitRequest customBenefitRequest) {
        switch (phase) {
            case PHASE_PROFILE:
                return BoltOnProfileRequest.builder().customBenefitRequest(customBenefitRequest).build();
            case PHASE_VERIFY:
                return BoltOnVerificationRequest.builder().customBenefitRequest(customBenefitRequest).build();
            case PHASE_ACTION:
                return BoltOnActionRequest.builder().customBenefitRequest(customBenefitRequest).build();
            default:
                throw new IllegalAccessError("Invalid phase value in configuration");
        }
    }

    private void invokeBoltOnService(String phase, BoltOnRequest request) throws Throwable {
        switch (phase) {
            case PHASE_PROFILE:
                this.soapProfileService.invokeAndUpdate((BoltOnProfileRequest) request);
                break;
            case PHASE_VERIFY:
                this.soapVerifyService.invokeAndUpdate((BoltOnVerificationRequest) request);
                break;
            case PHASE_ACTION:
                this.soapActionService.invokeAndUpdate((BoltOnActionRequest) request);
                break;
            default:
                throw new IllegalAccessError("Invalid phase value in configuration");
        }
    }

    private void updateBoltOnEventParams(String phase, AbstractOfferResponseEvent event, BoltOnRequest request) {
        switch (phase) {
            case PHASE_PROFILE:
                this.soapProfileService.updateEventParams(event, (BoltOnProfileRequest) request);
                break;
            case PHASE_VERIFY:
                this.soapVerifyService.updateEventParams(event, (BoltOnVerificationRequest) request);
                break;
            case PHASE_ACTION:
                this.soapActionService.updateEventParams(event, (BoltOnActionRequest) request);
                break;
        }
    }

    private void persist(String phase, BoltOnRequest request) {
        switch (phase) {
            case PHASE_PROFILE:
                this.persistenceProfileService.add((BoltOnProfileRequest) request);
                break;
            case PHASE_VERIFY:
                this.persistenceVerifyService.add((BoltOnVerificationRequest) request);
                break;
            case PHASE_ACTION:
                this.persistenceActionService.add((BoltOnActionRequest) request);
                break;
        }
    }
}
