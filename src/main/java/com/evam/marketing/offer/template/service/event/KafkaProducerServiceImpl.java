package com.evam.marketing.offer.template.service.event;

import com.evam.marketing.offer.template.configuration.kafka.property.KafkaProperties;
import com.evam.marketing.offer.template.service.event.model.CustomOfferResponseEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by cemserit on 2.03.2021.
 */
@Service
@Slf4j
public class KafkaProducerServiceImpl implements KafkaProducerService {

    private final KafkaTemplate<String, CustomOfferResponseEvent> kafkaTemplate;
    private final KafkaProperties kafkaProperties;
    private final KafkaCustomOfferResponseCallback eventCallback;
    private final String topicName;

    public KafkaProducerServiceImpl(KafkaTemplate<String, CustomOfferResponseEvent> kafkaTemplate,
                                    KafkaProperties kafkaProperties) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaProperties = kafkaProperties;
        this.eventCallback = new KafkaCustomOfferResponseCallback();
        this.topicName = kafkaProperties.getEventTopic().getName();
    }

    @Override
    public void sendEvent(CustomOfferResponseEvent event) {
        ProducerRecord<String, CustomOfferResponseEvent> record = new ProducerRecord<>(topicName, event.getActorId(), event);
        kafkaTemplate.send(record)
                .addCallback(eventCallback);
        log.debug("Kafka custom offer event successfully sent. {}", event);
    }
}
