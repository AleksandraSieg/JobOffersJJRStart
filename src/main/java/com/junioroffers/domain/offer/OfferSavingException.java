package com.junioroffers.domain.offer;

import java.util.List;

public class OfferSavingException extends RuntimeException {

    private final List<String> offerUrls;

    public OfferSavingException(String offerUrl) {
        super(String.format("Offer with offerUrl [%s] already exists", offerUrl));
        this.offerUrls = List.of(offerUrl);
    }

    public OfferSavingException(String msg, List<Offer> offers) {
        super(String.format("error" + msg + offers.toString()));
        this.offerUrls = offers.stream().map(Offer::offerUrl).toList();
    }
}
