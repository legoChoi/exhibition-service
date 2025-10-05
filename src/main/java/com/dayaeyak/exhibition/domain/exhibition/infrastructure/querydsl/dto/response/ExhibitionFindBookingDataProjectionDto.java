package com.dayaeyak.exhibition.domain.exhibition.infrastructure.querydsl.dto.response;

import com.dayaeyak.exhibition.domain.exhibition.domain.enums.Grade;
import com.querydsl.core.annotations.QueryProjection;

public record ExhibitionFindBookingDataProjectionDto(
        Integer price,

        Grade grade
) {

    @QueryProjection
    public ExhibitionFindBookingDataProjectionDto {
    }
}
