package com.dayaeyak.exhibition.domain.exhibition.application;

import com.dayaeyak.exhibition.common.exception.CustomRuntimeException;
import com.dayaeyak.exhibition.common.exception.type.ExhibitionExceptionType;
import com.dayaeyak.exhibition.domain.artist.Artist;
import com.dayaeyak.exhibition.domain.exhibition.domain.Exhibition;
import com.dayaeyak.exhibition.domain.exhibition.presentation.dto.request.ExhibitionCreateArtistRequestDto;
import com.dayaeyak.exhibition.domain.exhibition.presentation.dto.request.ExhibitionCreateRequestDto;
import com.dayaeyak.exhibition.domain.exhibition.presentation.dto.response.ExhibitionBookingDataResponseDto;
import com.dayaeyak.exhibition.domain.exhibition.presentation.dto.response.ExhibitionCreateResponseDto;
import com.dayaeyak.exhibition.domain.exhibition.presentation.dto.response.ExhibitionSearchPageResponseDto;
import com.dayaeyak.exhibition.domain.exhibition.domain.enums.Grade;
import com.dayaeyak.exhibition.domain.exhibition.domain.enums.Region;
import com.dayaeyak.exhibition.domain.exhibition.domain.enums.SearchType;
import com.dayaeyak.exhibition.domain.exhibition.infrastructure.jpa.ExhibitionJpaRepository;
import com.dayaeyak.exhibition.domain.exhibition.infrastructure.querydsl.ExhibitionQuerydslRepository;
import com.dayaeyak.exhibition.domain.exhibition.infrastructure.querydsl.dto.response.ExhibitionFindBookingDataProjectionDto;
import com.dayaeyak.exhibition.domain.exhibition.infrastructure.querydsl.dto.response.ExhibitionSearchProjectionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExhibitionInternalService {

    private final ExhibitionJpaRepository exhibitionJpaRepository;
    private final ExhibitionQuerydslRepository exhibitionQuerydslRepository;

    private final ExhibitionArtistMappingHelper exhibitionArtistMappingHelper;

    @Transactional
    public ExhibitionCreateResponseDto createExhibition(ExhibitionCreateRequestDto dto) {
        Exhibition exhibition = Exhibition.builder()
                .sellerId(dto.sellerId())
                .price(dto.price())
                .name(dto.name())
                .place(dto.place())
                .address(dto.address())
                .region(dto.region())
                .grade(dto.grade())
                .startDate(dto.startDate())
                .endDate(dto.endDate())
                .startTime(dto.startTime())
                .endTime(dto.endTime())
                .ticketOpenedAt(dto.ticketOpenedAt())
                .ticketClosedAt(dto.ticketClosedAt())
                .build();

        Exhibition createdExhibition = exhibitionJpaRepository.save(exhibition);

        Set<String> artistNames = dto.artists().stream()
                .map(ExhibitionCreateArtistRequestDto::name)
                .collect(Collectors.toSet());

        List<Artist> artists = exhibitionArtistMappingHelper.findOrCreateArtists(artistNames);
        exhibition.addExhibitionArtists(artists);

        return ExhibitionCreateResponseDto.from(createdExhibition, artists);
    }

    @Transactional(readOnly = true)
    public ExhibitionSearchPageResponseDto searchExhibition(
            int page,
            int size,
            Region region,
            Grade grade,
            LocalDate startDate,
            LocalDate endDate,
            String keyword,
            SearchType searchType
    ) {
        // endDate만 존재하는 경우 -> 에러
        if (startDate == null && endDate != null) {
            throw new CustomRuntimeException(ExhibitionExceptionType.DATE_RANGE_SEARCH_NEEDS_START_DATE);
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<ExhibitionSearchProjectionDto> data
                = exhibitionQuerydslRepository.searchExhibitions(pageable, region, grade, startDate, endDate, keyword, searchType);

        return ExhibitionSearchPageResponseDto.from(data);
    }

    public ExhibitionBookingDataResponseDto findExhibitionOrderData(Long exhibitionId) {
        ExhibitionFindBookingDataProjectionDto data = exhibitionQuerydslRepository.findExhibitionForBooking(exhibitionId)
                .orElseThrow(() -> new CustomRuntimeException(ExhibitionExceptionType.EXHIBITION_NOT_FOUND));

        return ExhibitionBookingDataResponseDto.from(data);
    }
}
