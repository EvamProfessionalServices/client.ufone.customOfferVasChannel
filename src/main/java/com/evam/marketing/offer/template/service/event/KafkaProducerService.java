package com.evam.marketing.offer.template.service.event;

import com.evam.marketing.offer.template.service.event.model.CustomOfferResponseEvent;

/**
 * Created by cemserit on 7.07.2021.
 */
public interface KafkaProducerService {
    void sendEvent(CustomOfferResponseEvent event);
}
