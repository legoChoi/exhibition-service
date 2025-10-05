package com.dayaeyak.exhibition.domain.exhibition.infrastructure.querydsl.dto.response;

import com.dayaeyak.exhibition.domain.exhibition.domain.enums.Grade;
import com.dayaeyak.exhibition.domain.exhibition.domain.enums.Region;
import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record ExhibitionFindProjectionDto(
        Long exhibitionId,

        Integer price,

        String name,

        String place,

        String address,

        Region region,

        Grade grade,

        LocalDate startDate,

        LocalDate endDate,

        LocalTime startTime,

        LocalTime endTime,

        LocalDateTime ticketOpenAt,

        LocalDateTime ticketCloseAt,

        Boolean isActivated,

        List<ExhibitionFindArtistProjectionDto> artists
) {

    @QueryProjection
    public ExhibitionFindProjectionDto {
    }
}
