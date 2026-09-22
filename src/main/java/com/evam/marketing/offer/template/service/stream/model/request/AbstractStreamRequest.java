package com.evam.marketing.offer.template.service.stream.model.request;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Created by cemserit on 15.04.2021.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class AbstractStreamRequest implements StreamRequest {

    private String name;
    @NotNull
    private String code;
    @NotNull
    private String offerUUID;
    @NotNull
    private String scenarioName;
    @NotNull
    private int scenarioVersion;
    @NotNull
    private String actorId;
    private String id;

    private String amount;
    private String spentAmount;
    private String offerType;
    private String offerStatus;
    private String statusDetail;
    private String segmentCode;
    private String trxId;
    @ToString.Exclude
    private List<OfferMerchant> merchants;
    @ToString.Exclude
    private List<OfferTag> tags;
    private String variant;
    private boolean controlGroup;
    private long insertDate;
    private long startDate;
    private long expireDate;
    private String channel;
}
