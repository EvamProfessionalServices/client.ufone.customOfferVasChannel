package com.evam.marketing.offer.template.service.integration;

import com.evam.marketing.offer.template.configuration.VasConfig;
import com.evam.marketing.offer.template.service.event.model.AbstractOfferResponseEvent;
import com.evam.marketing.offer.template.service.stream.model.request.BoltOnProfileRequest;
import com.evam.marketing.offer.template.utils.VasOfferUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The {@code SoapProfileService} interacts with Bolt On Vas service
 *
 * @author Abdul Wadood
 * @since 8.0.2
 */
@Service
@Log4j2
public class SoapProfileService {
    private static final String RESP_CODE = "code";
    private static final String RESP_DESCRIPTION = "description";
    private static final String RESP_PRODUCT_TYPE = "productType";
    private static final String RESP_DATA_RATE = "dataRate";
    private static final String RESP_VIRTUAL_NUMBER = "virtualNumber";
    private static final String RESP_INTEGRATION_ID = "integrationId";
    private static final String RESP_MOBILE_NUMBER = "mobileNumber";
    private static final String RESP_ACCOUNT_ID = "accountId";

    @Autowired
    private VasConfig config;

    /**
     * This method invokes a communication endpoint with the provided
     * BoltOnProfileRequest and updates its properties based on the response XML.
     * It first hits the endpoint using the provided request, obtaining an XML response.
     * Then, it extracts relevant information from the XML and updates the request object accordingly.
     * The extracted information includes response data, status, and description, which are set
     * within the CustomCommunicationRequest object.
     *
     * @param request the bolt on profile request
     * @throws Throwable throws an exception if any problem occurs
     */
    public void invokeAndUpdate(BoltOnProfileRequest request) throws Throwable {
        String payload = this.config.getPayloadProfile().replace(":landline", request.getLandline());
        log.debug("payload is {}", payload);
        String xml = VasOfferUtil.hit(this.config.getUrlProfile(), payload);
        log.debug("xml response is {}", xml);
        request.setResponse(xml);
        request.setCode(VasOfferUtil.extract(xml, this.config.getExprProCode()));
        request.setDescription(VasOfferUtil.extract(xml, this.config.getExprProDescription()));
        request.setProductType(VasOfferUtil.extract(xml, this.config.getExprProProductType()));
        request.setDataRate(VasOfferUtil.extract(xml, this.config.getExprProDataRate()));
        request.setVirtualNumber(VasOfferUtil.extract(xml, this.config.getExprProVirtualNumber()));
        request.setIntegrationId(VasOfferUtil.extract(xml, this.config.getExprProIntegrationId()));
        request.setMobileNumber(VasOfferUtil.extract(xml, this.config.getExprProMobileNumber()));
        request.setAccountId(VasOfferUtil.extract(xml, this.config.getExprProAccountId()));
    }

    public void updateEventParams(AbstractOfferResponseEvent event, BoltOnProfileRequest request) {
        event.addCustomParameter(RESP_CODE, request.getCode());
        event.addCustomParameter(RESP_DESCRIPTION, request.getDescription());
        event.addCustomParameter(RESP_PRODUCT_TYPE, request.getProductType());
        event.addCustomParameter(RESP_DATA_RATE, request.getDataRate());
        event.addCustomParameter(RESP_VIRTUAL_NUMBER, request.getVirtualNumber());
        event.addCustomParameter(RESP_INTEGRATION_ID, request.getIntegrationId());
        event.addCustomParameter(RESP_MOBILE_NUMBER, request.getMobileNumber());
        event.addCustomParameter(RESP_ACCOUNT_ID, request.getAccountId());
    }
}
