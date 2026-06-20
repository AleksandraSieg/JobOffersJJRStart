package com.junioroffers.domain.offer;

import com.junioroffers.domain.offer.dto.JobOfferResponse;

import java.util.List;

public class OfferFacadeTestConfiguration {

    private final InMemoryOfferRepository inMemoryOfferRepository;
    private final InMemoryFetcherTestImpl inMemoryFetcherTest;

    OfferFacadeTestConfiguration() {
        this.inMemoryFetcherTest = new InMemoryFetcherTestImpl(
                List.of(
                        new JobOfferResponse("abc1", "abc", "abc", "1"),
                        new JobOfferResponse("abc2", "abc", "abc", "2"),
                        new JobOfferResponse("abc3", "abc", "abc", "3"),
                        new JobOfferResponse("abc4", "abc", "abc", "4"),
                        new JobOfferResponse("abc5", "abc", "abc", "5"),
                        new JobOfferResponse("abc6", "abc", "abc", "6")
                )
        );
        this.inMemoryOfferRepository = new InMemoryOfferRepository();
    }

    OfferFacadeTestConfiguration(List<JobOfferResponse> remoteClientOffer) {
        this.inMemoryFetcherTest = new InMemoryFetcherTestImpl(remoteClientOffer);
        this.inMemoryOfferRepository = new InMemoryOfferRepository();
    }

    OfferFacade offerFacadeForTests() {
        return new OfferFacade(inMemoryOfferRepository, new OfferService(inMemoryOfferRepository, inMemoryFetcherTest));
    }
}
