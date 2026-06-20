package com.junioroffers.domain.offer;

import com.junioroffers.domain.offer.dto.JobOfferResponse;
import com.junioroffers.domain.offer.dto.OfferRequestDto;
import com.junioroffers.domain.offer.dto.OfferResponseDto;
import org.junit.Test;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;

import static org.junit.jupiter.api.Assertions.*;

public class  OfferFacadeTest {

    @Test
    public void should_fetch_from_jobs_from_remote_and_save_all_offers_when_rrepository_is_empty() {
        OfferFacade offerFacade = new OfferFacadeTestConfiguration().offerFacadeForTests();
        assertThat(offerFacade.findAllOffers().isEmpty());

        List<OfferResponseDto> result = offerFacade.fetchAllOffersAndSaveIfNotExists();

        assertThat(result).hasSize(6);
    }

    @Test
    public void should_save_6_offers_when_there_are_no_offers_in_database() {
        OfferFacade offerFacade = new OfferFacadeTestConfiguration(List.of()).offerFacadeForTests();

        offerFacade.saveOffer(new OfferRequestDto("a", "b", "c", "1"));
        offerFacade.saveOffer(new OfferRequestDto("a", "b", "c", "2"));
        offerFacade.saveOffer(new OfferRequestDto("a", "b", "c", "3"));
        offerFacade.saveOffer(new OfferRequestDto("a", "b", "c", "4"));
        offerFacade.saveOffer(new OfferRequestDto("a", "b", "c", "5"));
        offerFacade.saveOffer(new OfferRequestDto("a", "b", "c", "6"));

        assertThat(offerFacade.findAllOffers()).hasSize(6);
    }

    @Test
    public void should_save_only_3_offers_when_repository_had_5_added_with_offer_urls() {
        OfferFacade offerFacade = new OfferFacadeTestConfiguration(
                List.of(
                        new JobOfferResponse("a", "b", "c", "1"),
                        new JobOfferResponse("a", "b", "c", "2"),
                        new JobOfferResponse("a", "b", "c", "3"),
                        new JobOfferResponse("a", "b", "c", "4"),
                        new JobOfferResponse("a", "b", "c", "5"),
                        new JobOfferResponse("a", "b", "c", "offerUrl6"),
                        new JobOfferResponse("a", "b", "c", "offerUrl7"),
                        new JobOfferResponse("a", "b", "c", "offerUrl8")
                )
        ).offerFacadeForTests();
        offerFacade.saveOffer(new OfferRequestDto("a", "b", "c", "1"));
        offerFacade.saveOffer(new OfferRequestDto("a", "b", "c", "2"));
        offerFacade.saveOffer(new OfferRequestDto("a", "b", "c", "3"));
        offerFacade.saveOffer(new OfferRequestDto("a", "b", "c", "4"));
        offerFacade.saveOffer(new OfferRequestDto("a", "b", "c", "5"));
        assertThat(offerFacade.findAllOffers()).hasSize(5);

        List<OfferResponseDto> responseDtos = offerFacade.fetchAllOffersAndSaveIfNotExists();

        assertThat(List.of(
                responseDtos.get(0).offerUrl(),
                responseDtos.get(1).offerUrl(),
                responseDtos.get(2).offerUrl()
        )).containsExactlyInAnyOrder("offerUrl6", "offerUrl7", "offerUrl8");
    }

    @Test
    public void should_find_offer_by_id_when_offer_is_in_database() {
        OfferFacade offerFacade = new OfferFacadeTestConfiguration(List.of()).offerFacadeForTests();
        OfferResponseDto offerResponseDto = offerFacade.saveOffer(new OfferRequestDto("id1", "abc", "abc", "Url3"));

        OfferResponseDto offerById = offerFacade.findOfferById(offerResponseDto.id());

        assertThat(offerById).isEqualTo(OfferResponseDto.builder()
                .id(offerResponseDto.id())
                .companyName("id1")
                .position("abc")
                .salary("abc")
                .offerUrl("Url3")
                .build()
        );
    }

    @Test
    public void should_throw_not_found_exception_when_offer_not_found() {
        OfferFacade offerFacade = new OfferFacadeTestConfiguration(List.of()).offerFacadeForTests();

        assertThat(offerFacade.findAllOffers()).isEmpty();

        Throwable throwable = catchThrowable(() -> offerFacade.findOfferById("300"));

        AssertionsForClassTypes.assertThat(throwable)
                .isInstanceOf(OfferNotFoundException.class)
                .hasMessage("Offer with id 300 not found");
    }

    @Test
    public void should_throw_duplicate_key_exception_when_with_offer_url_exists() {
        OfferFacade offerFacade = new OfferFacadeTestConfiguration(List.of()).offerFacadeForTests();

        OfferResponseDto offerResponseDto = offerFacade.saveOffer(new OfferRequestDto("id", "cde", "cde", "cde.pl"));
        String saveId = offerResponseDto.id();

        assertThat(offerFacade.findOfferById(saveId).id()).isEqualTo(saveId);

        Throwable throwable = catchThrowable(() -> offerFacade.saveOffer(
                new OfferRequestDto("id2", "cdef", "cdef", "cde.pl")));

        AssertionsForClassTypes.assertThat(throwable)
                .isInstanceOf(OfferDuplicateException.class)
                .hasMessage("Offer with offerUrl [cde.pl] already exists");
    }

}