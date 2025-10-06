package com.dayaeyak.exhibition.domain.artist.presentation.dto.response;

import com.dayaeyak.exhibition.common.dto.PageInfoResponseDto;
import com.dayaeyak.exhibition.domain.artist.domain.Artist;
import org.springframework.data.domain.Page;

import java.util.List;

public record ArtistSearchPageResponseDto(
        PageInfoResponseDto pageInfo,

        List<ArtistSearchResponseDto> data
) {

    public static ArtistSearchPageResponseDto from(Page<Artist> page) {
        PageInfoResponseDto pageInfo = PageInfoResponseDto.from(page);

        List<ArtistSearchResponseDto> data = page.getContent()
                .stream().map(ArtistSearchResponseDto::from)
                .toList();

        return new ArtistSearchPageResponseDto(pageInfo, data);
    }
}
