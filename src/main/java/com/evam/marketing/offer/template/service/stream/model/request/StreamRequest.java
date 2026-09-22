package com.evam.marketing.offer.template.service.stream.model.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

/**
 * Created by cemserit on 3.03.2021.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, defaultImpl = CustomBenefitRequest.class, property = "offerType")
@JsonSubTypes({@JsonSubTypes.Type(value = CustomBenefitRequest.class, name = "CUSTOM_BENEFIT")})
public interface StreamRequest {

    String getChannel();

    String getName();

    String getCode();

    String getOfferUUID();

    String getId();

    String getAmount();

    String getSpentAmount();

    String getScenarioName();

    int getScenarioVersion();

    String getActorId();

    String getOfferType();

    String getOfferStatus();

    String getStatusDetail();

    String getSegmentCode();

    String getTrxId();

    List<OfferMerchant> getMerchants();

    List<OfferTag> getTags();

    String getVariant();

    boolean isControlGroup();

    long getInsertDate();

    long getStartDate();

    long getExpireDate();
}
