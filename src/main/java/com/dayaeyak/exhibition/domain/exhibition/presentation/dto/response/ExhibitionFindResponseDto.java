package com.dayaeyak.exhibition.domain.exhibition.presentation.dto.response;

import com.dayaeyak.exhibition.domain.exhibition.domain.enums.Grade;
import com.dayaeyak.exhibition.domain.exhibition.domain.enums.Region;
import com.dayaeyak.exhibition.domain.exhibition.infrastructure.querydsl.dto.response.ExhibitionFindProjectionDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Builder
public record ExhibitionFindResponseDto(
        Long exhibitionId,

        Integer price,

        String name,

        String place,

        String address,

        Region region,

        Grade grade,

        @JsonFormat(pattern = "yyyy년 MM월 dd일")
        LocalDate startDate,

        @JsonFormat(pattern = "yyyy년 MM월 dd일")
        LocalDate endDate,

        @JsonFormat(pattern = "HH시 mm분")
        LocalTime startTime,

        @JsonFormat(pattern = "HH시 mm분")
        LocalTime endTime,

        @JsonFormat(pattern = "yyyy년 MM월 dd일 HH시 mm분")
        LocalDateTime ticketOpenAt,

        @JsonFormat(pattern = "yyyy년 MM월 dd일 HH시 mm분")
        LocalDateTime ticketCloseAt,

        Boolean isActivated,

        List<ExhibitionFindArtistResponseDto> artists
) {

    public static ExhibitionFindResponseDto from(ExhibitionFindProjectionDto record) {
        List<ExhibitionFindArtistResponseDto> artists = record.artists().stream()
                .map(ExhibitionFindArtistResponseDto::from)
                .toList();

        return ExhibitionFindResponseDto.builder()
                .exhibitionId(record.exhibitionId())
                .price(record.price())
                .name(record.name())
                .place(record.place())
                .address(record.address())
                .region(record.region())
                .grade(record.grade())
                .startDate(record.startDate())
                .endDate(record.endDate())
                .startTime(record.startTime())
                .endTime(record.endTime())
                .ticketOpenAt(record.ticketOpenAt())
                .ticketCloseAt(record.ticketCloseAt())
                .isActivated(record.isActivated())
                .artists(artists)
                .build();
    }
}
