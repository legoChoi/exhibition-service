package com.dayaeyak.exhibition.domain.exhibition.presentation.dto.response;

import com.dayaeyak.exhibition.domain.exhibition.infrastructure.querydsl.dto.response.ExhibitionFindArtistProjectionDto;

public record ExhibitionFindArtistResponseDto(
        Long artistId,

        String name
) {

    public static ExhibitionFindArtistResponseDto from(ExhibitionFindArtistProjectionDto dto) {
        return new ExhibitionFindArtistResponseDto(dto.artistId(), dto.name());
    }
}
