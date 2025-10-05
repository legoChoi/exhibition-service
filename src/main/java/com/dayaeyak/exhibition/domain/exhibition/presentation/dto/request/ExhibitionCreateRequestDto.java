package com.dayaeyak.exhibition.domain.exhibition.presentation.dto.request;

import com.dayaeyak.exhibition.domain.exhibition.domain.enums.Grade;
import com.dayaeyak.exhibition.domain.exhibition.domain.enums.Region;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Builder
public record ExhibitionCreateRequestDto(
        Long sellerId,

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

        LocalDateTime ticketOpenedAt,

        LocalDateTime ticketClosedAt,

        List<ExhibitionCreateArtistRequestDto> artists
) {
}
