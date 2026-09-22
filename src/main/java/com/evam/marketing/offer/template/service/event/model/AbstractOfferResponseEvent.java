package com.evam.marketing.offer.template.service.event.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Created by cemserit on 2.03.2021.
 */
@Getter
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode
public abstract class AbstractOfferResponseEvent implements CustomOfferResponseEvent {

    private String name;
    private String scenario;
    private String actorId;
    private String message;
    private String offerUUID;
    private String offerCode;
    private String offerName;
    private String offerChannel;

    private Map<String, Object> customParameters;

    @Override
    public void addCustomParameter(String parameterKey, String parameterValue) {
        if (customParameters == null) {
            customParameters = new HashMap<>();
        }
        customParameters.put(parameterKey, parameterValue);
    }

    @Override
    public void addCustomParameter(String parameterKey, BigDecimal parameterValue) {
        if (customParameters == null) {
            customParameters = new HashMap<>();
        }
        customParameters.put(parameterKey, parameterValue);
    }

    @JsonAnyGetter
    @Override
    public Map<String, Object> getCustomParameters() {
        if (customParameters == null) {
            return Collections.emptyMap();
        }
        return customParameters;
    }
}
