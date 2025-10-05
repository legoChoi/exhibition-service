package com.dayaeyak.exhibition.domain.exhibition.presentation.dto.response;

import com.dayaeyak.exhibition.common.dto.PageInfoResponseDto;
import com.dayaeyak.exhibition.domain.exhibition.infrastructure.querydsl.dto.response.ExhibitionSearchProjectionDto;
import org.springframework.data.domain.Page;

import java.util.List;

public record ExhibitionSearchPageResponseDto(
        PageInfoResponseDto pageInfo,

        List<ExhibitionSearchResponseDto> data
) {

    public static ExhibitionSearchPageResponseDto from(Page<ExhibitionSearchProjectionDto> page) {
        PageInfoResponseDto pagInfo = PageInfoResponseDto.from(page);

        List<ExhibitionSearchResponseDto> data = page.getContent().stream()
                .map(ExhibitionSearchResponseDto::from)
                .toList();

        return new ExhibitionSearchPageResponseDto(pagInfo, data);
    }
}
