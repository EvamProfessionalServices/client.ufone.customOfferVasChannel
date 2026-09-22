package com.evam.marketing.offer.template.service.event;

import com.evam.marketing.offer.template.service.event.model.CustomOfferResponseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * Created by cemserit on 2.03.2021.
 */
@Slf4j
public class KafkaCustomOfferResponseCallback implements ListenableFutureCallback<SendResult<String,
        CustomOfferResponseEvent>> {
    @Override
    public void onFailure(Throwable ex) {
        log.warn("Kafka message send fail!", ex);
    }

    @Override
    public void onSuccess(SendResult<String, CustomOfferResponseEvent> result) {
        log.info("Kafka message successfully sent. {}", result);
    }
}
