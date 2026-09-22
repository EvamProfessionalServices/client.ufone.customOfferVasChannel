package com.evam.marketing.offer.template.service.event.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * The {@code CustomOfferFailEvent} class
 *
 * @author Abdul Wadood
 * @since 8.0.2
 */
@SuperBuilder
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CustomOfferFailEvent extends AbstractOfferResponseEvent {
    private String reason;

    @Override
    public PushNotificationEventType getType() {
        return PushNotificationEventType.FAIL;
    }
}
