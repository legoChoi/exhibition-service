package com.dayaeyak.exhibition.domain.exhibition.presentation.dto.response;

import com.dayaeyak.exhibition.domain.exhibition.domain.enums.Grade;
import com.dayaeyak.exhibition.domain.exhibition.infrastructure.querydsl.dto.response.ExhibitionFindBookingDataProjectionDto;

public record ExhibitionBookingDataResponseDto(
        Integer price,

        Grade grade
) {

    public static ExhibitionBookingDataResponseDto from(ExhibitionFindBookingDataProjectionDto data) {
        return new ExhibitionBookingDataResponseDto(data.price(), data.grade());
    }
}
