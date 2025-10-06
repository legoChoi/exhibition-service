package com.dayaeyak.exhibition.domain.exhibition.presentation.dto.response;

import com.dayaeyak.exhibition.domain.artist.domain.Artist;

public record ExhibitionCreateArtistInfoResponseDto(
        Long artistId,

        String name
) {

    public static ExhibitionCreateArtistInfoResponseDto from(Artist artist) {
        return new ExhibitionCreateArtistInfoResponseDto(artist.getId(), artist.getName());
    }
}
