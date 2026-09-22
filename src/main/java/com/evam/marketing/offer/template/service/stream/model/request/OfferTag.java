package com.evam.marketing.offer.template.service.stream.model.request;

import lombok.*;

import java.io.Serializable;

/**
 * Created by cemserit on 1.06.2021.
 */
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OfferTag implements Serializable {
    private String name;
    private String value;
}
