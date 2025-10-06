package com.dayaeyak.exhibition.domain.artist.presentation.dto.response;

import com.dayaeyak.exhibition.domain.artist.domain.Artist;

public record ArtistCreateResponseDto(
        Long artistId,

        String name
) {

    public static ArtistCreateResponseDto from(Artist artist) {
        return new ArtistCreateResponseDto(artist.getId(), artist.getName());
    }
}
