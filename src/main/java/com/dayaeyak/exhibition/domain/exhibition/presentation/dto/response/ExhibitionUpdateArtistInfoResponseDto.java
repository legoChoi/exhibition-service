package com.dayaeyak.exhibition.domain.exhibition.presentation.dto.response;

import com.dayaeyak.exhibition.domain.artist.Artist;

public record ExhibitionUpdateArtistInfoResponseDto(
        Long artistId,

        String name
) {

    public static ExhibitionUpdateArtistInfoResponseDto from(Artist artist) {
        return new ExhibitionUpdateArtistInfoResponseDto(artist.getId(), artist.getName());
    }
}
