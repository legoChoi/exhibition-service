package com.dayaeyak.exhibition.domain.artist.presentation.dto.response;

import com.dayaeyak.exhibition.domain.artist.domain.Artist;

public record ArtistUpdateResponseDto(
        Long artisId,

        String name
) {

    public static ArtistUpdateResponseDto from(Artist artist) {
        return new ArtistUpdateResponseDto(artist.getId(), artist.getName());
    }
}
