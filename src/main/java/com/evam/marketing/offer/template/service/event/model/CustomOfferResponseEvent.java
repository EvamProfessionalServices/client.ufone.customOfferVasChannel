package com.evam.marketing.offer.template.service.event.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by cemserit on 5.03.2021.
 */
public interface CustomOfferResponseEvent extends Serializable {

    String getName();

    String getScenario();

    String getActorId();

    String getOfferName();

    String getOfferCode();

    String getOfferUUID();

    PushNotificationEventType getType();

    void addCustomParameter(String parameterKey, String parameterValue);

    void addCustomParameter(String parameterKey, BigDecimal parameterValue);

    Map<String, Object> getCustomParameters();
}
