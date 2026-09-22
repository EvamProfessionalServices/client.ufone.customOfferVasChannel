package com.evam.marketing.offer.template.service.integration;

import com.evam.marketing.offer.template.service.stream.model.request.StreamRequest;

/**
 * Created by cemserit on 11.03.2021.
 */
public interface OfferService {

    void execute(StreamRequest streamRequest);
}
