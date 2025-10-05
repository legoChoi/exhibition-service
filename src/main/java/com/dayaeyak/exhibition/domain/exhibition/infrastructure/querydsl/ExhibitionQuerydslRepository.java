package com.dayaeyak.exhibition.domain.exhibition.infrastructure.querydsl;

import com.dayaeyak.exhibition.domain.exhibition.domain.Exhibition;
import com.dayaeyak.exhibition.domain.exhibition.domain.enums.Grade;
import com.dayaeyak.exhibition.domain.exhibition.domain.enums.Region;
import com.dayaeyak.exhibition.domain.exhibition.domain.enums.SearchType;
import com.dayaeyak.exhibition.domain.exhibition.infrastructure.querydsl.dto.response.ExhibitionFindBookingDataProjectionDto;
import com.dayaeyak.exhibition.domain.exhibition.infrastructure.querydsl.dto.response.ExhibitionFindProjectionDto;
import com.dayaeyak.exhibition.domain.exhibition.infrastructure.querydsl.dto.response.ExhibitionSearchProjectionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface ExhibitionQuerydslRepository {

    /**
     * 단건 조회용
     */
    Optional<ExhibitionFindProjectionDto> findExhibitionDetail(Long exhibitionId);

    /**
     * 수정용
     */
    Optional<Exhibition> findExhibitionById(Long exhibitionId);

    /**
     * 검색용
     */
    Page<ExhibitionSearchProjectionDto> searchExhibitions(Pageable pageable, Region region, Grade grade, LocalDate startDate, LocalDate endDate, String keyword, SearchType searchType);

    /**
     * 예약용
     */
    Optional<ExhibitionFindBookingDataProjectionDto> findExhibitionForBooking(Long exhibitionId);
}
