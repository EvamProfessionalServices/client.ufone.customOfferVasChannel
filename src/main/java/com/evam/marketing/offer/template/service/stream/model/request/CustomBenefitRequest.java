package com.evam.marketing.offer.template.service.stream.model.request;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Abdul Wadood
 * @since 8.0.2
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CustomBenefitRequest extends AbstractStreamRequest {
    @ToString.Exclude
    private List<OfferCustomBenefitParameter> customBenefitParameters;
    private String customBenefitTemplate;

    public List<OfferCustomBenefitParameter> getCustomBenefitParameters() {
        if (Objects.isNull(customBenefitParameters)) {
            return Collections.emptyList();
        }
        return customBenefitParameters;
    }
}
