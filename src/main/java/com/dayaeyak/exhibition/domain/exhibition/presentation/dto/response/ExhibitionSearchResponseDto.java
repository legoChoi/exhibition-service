package com.dayaeyak.exhibition.domain.exhibition.presentation.dto.response;

import com.dayaeyak.exhibition.domain.exhibition.domain.enums.Grade;
import com.dayaeyak.exhibition.domain.exhibition.domain.enums.Region;
import com.dayaeyak.exhibition.domain.exhibition.infrastructure.querydsl.dto.response.ExhibitionSearchProjectionDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
public record ExhibitionSearchResponseDto(
        Long exhibitionId,

        String name,

        String place,

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
        LocalDateTime ticketCloseAt
) {

    public static ExhibitionSearchResponseDto from(ExhibitionSearchProjectionDto data) {
        return ExhibitionSearchResponseDto.builder()
                .exhibitionId(data.exhibitionId())
                .name(data.name())
                .place(data.place())
                .region(data.region())
                .grade(data.grade())
                .startDate(data.startDate())
                .endDate(data.endDate())
                .startTime(data.startTime())
                .endTime(data.endTime())
                .ticketOpenAt(data.ticketOpenAt())
                .ticketCloseAt(data.ticketCloseAt())
                .build();
    }
}
