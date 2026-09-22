package com.evam.marketing.offer.template.service.stream.model.request;

public interface BoltOnRequest {
    void updateRequestParams();
    String getSilentMode();
    CustomBenefitRequest getCustomBenefitRequest();
}
