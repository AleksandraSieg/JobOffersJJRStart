package com.junioroffers;

public interface SampleJobOfferResponse {

    default String bodyWithZeroOffersJson() {
        return "[]";
    }
}
