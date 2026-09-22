package com.evam.marketing.offer.template.service.event.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * The {@code CustomOfferSuccessEvent} class
 *
 * @author Abdul Wadood
 * @since 8.0.2
 */
@Getter
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CustomOfferSuccessEvent extends AbstractOfferResponseEvent {
    @Override
    public PushNotificationEventType getType() {
        return PushNotificationEventType.SUCCESS;
    }
}
