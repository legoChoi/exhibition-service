package com.dayaeyak.exhibition.domain.artist.presentation.dto.response;

import com.dayaeyak.exhibition.domain.artist.domain.Artist;

public record ArtistSearchResponseDto(
        Long artistId,

        String name
) {

    public static ArtistSearchResponseDto from(Artist artist) {
        return new ArtistSearchResponseDto(artist.getId(), artist.getName());
    }
}
