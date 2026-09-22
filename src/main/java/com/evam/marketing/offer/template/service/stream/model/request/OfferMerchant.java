package com.evam.marketing.offer.template.service.stream.model.request;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OfferMerchant implements Serializable {
    private String name;
    private String offerRegisterCode;
}
