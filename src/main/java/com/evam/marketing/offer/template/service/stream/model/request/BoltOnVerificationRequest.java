package com.evam.marketing.offer.template.service.stream.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The {@code BoltonVerificationRequest}
 *
 * @author Abdul Wadood
 * @since 8.0.2
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class BoltOnVerificationRequest implements BoltOnRequest {
    private CustomBenefitRequest customBenefitRequest;

    //variables for parameters
    private String partNumber;
    private String virtualNumber;
    private String action;
    private String silentMode;

    //response variables
    private String code;
    private String description;
    private String integrationId;
    private String installedVas;
    private String response;

    @Override
    public void updateRequestParams() {
        for (OfferCustomBenefitParameter param : this.getCustomBenefitRequest().getCustomBenefitParameters()) {
            String name = param.getName();
            switch (name) {
                case "PARTNUMBER":
                    this.partNumber = param.getValue();
                    break;
                case "VIRTUALNUMBER":
                    this.virtualNumber = param.getValue();
                    break;
                case "ACTION":
                    this.action = param.getValue();
                    break;
                case "DEBUGMODE":
                    this.silentMode = param.getValue();
                    break;
            }
        }
    }
}
