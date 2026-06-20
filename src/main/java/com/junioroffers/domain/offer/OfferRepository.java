package com.junioroffers.domain.offer;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OfferRepository {

    boolean existsByOfferUrl(String offerUrl);

    Optional<Offer> findByOfferUrls(String offerUrl);

    List<Offer> saveAll(List<Offer> offers);

    List<Offer> findAll();

    Optional<Offer> findById(String id);

    Offer save(Offer offer);
}
