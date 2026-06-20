package com.junioroffers.domain.offer;

import com.junioroffers.domain.offer.dto.JobOfferResponse;

import java.util.List;

public class InMemoryFetcherTestImpl implements OfferFetchable {

    List<JobOfferResponse> listOfOffers;

    InMemoryFetcherTestImpl(List<JobOfferResponse> fetchOffers) {
        this.listOfOffers = fetchOffers;
    }

    @Override
    public List<JobOfferResponse> fetchOffers() {
        return listOfOffers;
    }
}
