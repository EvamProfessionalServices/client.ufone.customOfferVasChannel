package com.evam.marketing.offer.template.service.integration;

import com.evam.marketing.offer.template.configuration.VasConfig;
import com.evam.marketing.offer.template.service.event.model.AbstractOfferResponseEvent;
import com.evam.marketing.offer.template.service.stream.model.request.BoltOnVerificationRequest;
import com.evam.marketing.offer.template.utils.VasOfferUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The {@code SoapVerifyService} interacts with Bolt On Vas service
 *
 * @author Abdul Wadood
 * @since 8.0.2
 */
@Service
@Log4j2
public class SoapVerifyService {
    private static final String RESP_CODE = "code";
    private static final String RESP_DESCRIPTION = "description";
    private static final String RESP_INTEGRATION_ID = "integrationId";
    private static final String RESP_INSTALLED_VAS = "installedVas";

    @Autowired
    private VasConfig config;

    /**
     * This method invokes a communication endpoint with the provided
     * BoltOnVerificationRequest and updates its properties based on the response XML.
     * It first hits the endpoint using the provided request, obtaining an XML response.
     * Then, it extracts relevant information from the XML and updates the request object accordingly.
     * The extracted information includes response data, status, and description, which are set
     * within the CustomCommunicationRequest object.
     *
     * @param request the bolt on verification request
     * @throws Throwable throws an exception if any problem occurs
     */
    public void invokeAndUpdate(BoltOnVerificationRequest request) throws Throwable {
        String payload = this.config.getPayloadVerify()
                .replace(":partNumber", request.getPartNumber())
                .replace(":virtualNumber", request.getVirtualNumber())
                .replace(":actionType", request.getAction());
        log.debug("payload is {}", payload);
        String xml = VasOfferUtil.hit(this.config.getUrlVerify(), payload);
        log.debug("xml response is {}", xml);
        request.setResponse(xml);
        request.setCode(VasOfferUtil.extract(xml, this.config.getExprVerCode()));
        request.setDescription(VasOfferUtil.extract(xml, this.config.getExprVerDesc()));
        request.setIntegrationId(VasOfferUtil.extract(xml, this.config.getExprVerIntegrationId()));
        String installedVas = VasOfferUtil.extract(xml, this.config.getExprVerVasList());
        installedVas = String.join(",VAS_", installedVas.split("VAS_"));
        request.setInstalledVas(installedVas.isEmpty() ? "" : installedVas.substring(1));
    }

    public void updateEventParams(AbstractOfferResponseEvent event, BoltOnVerificationRequest request) {
        event.addCustomParameter(RESP_CODE, request.getCode());
        event.addCustomParameter(RESP_DESCRIPTION, request.getDescription());
        event.addCustomParameter(RESP_INTEGRATION_ID, request.getIntegrationId());
        event.addCustomParameter(RESP_INSTALLED_VAS, request.getInstalledVas());
    }
}
