package com.evam.marketing.offer.template.service.integration;

import com.evam.marketing.offer.template.service.event.KafkaProducerServiceImpl;
import com.evam.marketing.offer.template.service.event.model.CustomOfferFailEvent;
import com.evam.marketing.offer.template.service.event.model.CustomOfferResponseEvent;
import com.evam.marketing.offer.template.service.event.model.CustomOfferSuccessEvent;
import com.evam.marketing.offer.template.service.stream.model.request.StreamRequest;
import lombok.RequiredArgsConstructor;

/**
 * The {@code AbstractOfferService} class
 *
 * @author Abdul Wadood
 * @since 8.0.2
 */
@RequiredArgsConstructor
public abstract class AbstractOfferService implements OfferService {

    private final KafkaProducerServiceImpl kafkaProducerService;

    void sendEvent(CustomOfferResponseEvent event) {
        kafkaProducerService.sendEvent(event);
    }

    CustomOfferSuccessEvent toCustomOfferSuccessEvent(String eventName, StreamRequest request, String message) {
        return CustomOfferSuccessEvent.builder()
                .scenario(request.getScenarioName())
                .actorId(request.getActorId())
                .offerCode(request.getCode())
                .offerUUID(request.getOfferUUID())
                .offerName(request.getName())
                .offerChannel(request.getChannel())
                .message(message)
                .name(eventName)
                .build();
    }

    CustomOfferFailEvent toCustomOfferFailEvent(String eventName, StreamRequest request, String message, String reason) {
        return CustomOfferFailEvent.builder()
                .scenario(request.getScenarioName())
                .actorId(request.getActorId())
                .offerCode(request.getCode())
                .offerUUID(request.getOfferUUID())
                .offerName(request.getName())
                .message(message)
                .reason(reason)
                .name(eventName)
                .build();
    }
}
