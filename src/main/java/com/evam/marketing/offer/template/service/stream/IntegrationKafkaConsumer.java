package com.evam.marketing.offer.template.service.stream;

import com.evam.marketing.offer.template.service.integration.OfferService;
import com.evam.marketing.offer.template.service.stream.model.request.StreamRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * Created by cemserit on 3.03.2021.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class IntegrationKafkaConsumer {

    public static final String LISTENER_ID = "INTEGRATION_LISTENER";

    private final OfferService offerService;

    @KafkaListener(id = LISTENER_ID,
            topics = {"${kafka.integration-topic.name}"},
            groupId = "${kafka.integration-topic.group}",
            containerFactory = "integrationKafkaListenerContainerFactory"
    )
    public void integrationListener(List<StreamRequest> requests, Acknowledgment ack) {
        try {
            log.info("Received offer request records [{}]. {}", requests.size(), requests);

            //if needed, duplicates can be removed here

            for (StreamRequest streamRequest : requests) {
                offerService.execute(streamRequest);
                log.debug("Request successfully executed. {}", streamRequest);
            }
        } finally {
            ack.acknowledge();
        }
    }
}
