package com.evam.marketing.offer.template.service.integration;

import com.evam.marketing.offer.template.configuration.VasConfig;
import com.evam.marketing.offer.template.service.event.model.AbstractOfferResponseEvent;
import com.evam.marketing.offer.template.service.stream.model.request.BoltOnActionRequest;
import com.evam.marketing.offer.template.utils.VasOfferUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The {@code SoapActionService} interacts with Bolt On Vas service
 *
 * @author Abdul Wadood
 * @since 8.0.2
 */
@Service
@Log4j2
public class SoapActionService {
    private static final String RESP_CODE = "errorCode";
    private static final String RESP_MSG = "errorMessage";
    private static final String RESP_ORDER_ID = "orderId";
    private static final String RESP_STATUS = "status";

    @Autowired
    private VasConfig config;

    /**
     * This method invokes a communication endpoint with the provided
     * BoltOnActionRequest and updates its properties based on the response XML.
     * It first hits the endpoint using the provided request, obtaining an XML response.
     * Then, it extracts relevant information from the XML and updates the request object accordingly.
     * The extracted information includes response data, status, and description, which are set
     * within the CustomCommunicationRequest object.
     *
     * @param request
     * @throws Throwable
     */
    public void invokeAndUpdate(BoltOnActionRequest request) throws Throwable {

        String channelName = request.getChannel();
        VasConfig.Channel channel = this.config.getChannels()
                .get(channelName);

        if (channel == null) {
            channel = this.config.getChannels()
                    .get("DEFAULT");
        }

        boolean isAddVas = request.getAction().equalsIgnoreCase("add");
        String endpoint = isAddVas ? this.config.getUrlActionAdd() : this.config.getUrlActionRemove();
        String payload = (isAddVas ? this.config.getPayloadAddVas() : this.config.getPayloadRemoveVas())
                .replace(":landline", request.getLandline())
                .replace(":integrationId", request.getIntegrationId())
                .replace(":partNumber", request.getPartNumber())
                .replace(":userName", channel.getUsername())
                .replace(":password", channel.getPassword())
                .replace(":ip", channel.getIp());
        log.debug("payload is {}", payload);
        String xml = VasOfferUtil.hit(endpoint, payload);
        log.debug("xml response is {}", xml);
        request.setResponse(xml);
        request.setErrorCode(VasOfferUtil.extract(xml, this.config.getExprActCode()));
        request.setErrorMessage(VasOfferUtil.extract(xml, this.config.getExprActMessage()));
        request.setOrderId(VasOfferUtil.extract(xml, this.config.getExprActOrderId()));
        request.setStatus(VasOfferUtil.extract(xml, this.config.getExprActStatus()));

        if (request.getStatus().equalsIgnoreCase("submitted")) {
            throw new Exception("status is not submitted, assuming it a fail case.");
        }
    }

    public void updateEventParams(AbstractOfferResponseEvent event, BoltOnActionRequest request) {
        event.addCustomParameter(RESP_CODE, request.getErrorCode());
        event.addCustomParameter(RESP_MSG, request.getErrorMessage());
        event.addCustomParameter(RESP_ORDER_ID, request.getOrderId());
        event.addCustomParameter(RESP_STATUS, request.getStatus());
    }
}
