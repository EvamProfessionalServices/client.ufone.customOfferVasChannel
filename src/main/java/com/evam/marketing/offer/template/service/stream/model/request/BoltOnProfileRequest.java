package com.evam.marketing.offer.template.service.stream.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The {@code BoltonProfileRequest}
 *
 * @author Abdul Wadood
 * @since 8.0.2
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class BoltOnProfileRequest implements BoltOnRequest {
    private CustomBenefitRequest customBenefitRequest;

    //variables for parameters
    private String landline;
    private String silentMode;

    //response variables
    private String code;
    private String description;
    private String productType;
    private String dataRate;
    private String virtualNumber;
    private String integrationId;
    private String mobileNumber;
    private String accountId;
    private String response;

    @Override
    public void updateRequestParams() {
        for (OfferCustomBenefitParameter param : this.getCustomBenefitRequest().getCustomBenefitParameters()) {
            String name = param.getName();
            switch (name) {
                case "MDN":
                    this.landline = param.getValue();
                    break;
                case "DEBUGMODE":
                    this.silentMode = param.getValue();
                    break;
            }
        }
    }
}
