package com.dayaeyak.exhibition.domain.exhibition.presentation.dto.request;

import com.dayaeyak.exhibition.domain.exhibition.domain.enums.Grade;
import com.dayaeyak.exhibition.domain.exhibition.domain.enums.Region;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record ExhibitionUpdateRequestDto(
        String name,

        Integer price,

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

        List<ExhibitionUpdateArtistRequestDto> artists
) {

    public Set<String> getUpdateArtistNameSet() {
        return this.artists.stream()
                .map(ExhibitionUpdateArtistRequestDto::name)
                .collect(Collectors.toSet());
    }
}
