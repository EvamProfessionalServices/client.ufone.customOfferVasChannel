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
public class BoltOnActionRequest implements BoltOnRequest {
    private CustomBenefitRequest customBenefitRequest;

    //variables for parameters
    private String action;
    private String landline;
    private String integrationId;
    private String partNumber;
    private String silentMode;
    private String channel;

    //response variables
    private String errorCode;
    private String errorMessage;
    private String orderId;
    private String status;
    private String response;

    @Override
    public void updateRequestParams() {
        for (OfferCustomBenefitParameter param : this.getCustomBenefitRequest().getCustomBenefitParameters()) {
            String name = param.getName();
            switch (name) {
                case "ACTION":
                    this.action = param.getValue();
                    break;
                case "PSTN":
                    this.landline = param.getValue();
                    break;
                case "INTEGRATIONID":
                    this.integrationId = param.getValue();
                    break;
                case "SERVICEID":
                    this.partNumber = param.getValue();
                    break;
                case "DEBUGMODE":
                    this.silentMode = param.getValue();
                    break;
                case "CHANNEL_TYPE":
                    this.channel = param.getValue();
                    break;
            }
        }
    }
}
