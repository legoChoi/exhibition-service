package com.dayaeyak.exhibition.domain.exhibition.application;

import com.dayaeyak.exhibition.common.exception.CustomRuntimeException;
import com.dayaeyak.exhibition.common.exception.type.ExhibitionExceptionType;
import com.dayaeyak.exhibition.domain.exhibition.domain.Exhibition;
import com.dayaeyak.exhibition.domain.exhibition.presentation.dto.request.ExhibitionUpdateRequestDto;
import com.dayaeyak.exhibition.domain.exhibition.presentation.dto.response.ExhibitionUpdateActivationResponseDto;
import com.dayaeyak.exhibition.domain.exhibition.presentation.dto.response.ExhibitionUpdateResponseDto;
import com.dayaeyak.exhibition.domain.exhibition.infrastructure.jpa.ExhibitionJpaRepository;
import com.dayaeyak.exhibition.domain.exhibition.infrastructure.querydsl.ExhibitionQuerydslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExhibitionAdminService {

    private final ExhibitionJpaRepository exhibitionJpaRepository;
    private final ExhibitionQuerydslRepository exhibitionQuerydslRepository;

    private final ExhibitionArtistMappingHelper exhibitionArtistMappingHelper;


    @Transactional
    public ExhibitionUpdateResponseDto updateExhibition(Long exhibitionId, ExhibitionUpdateRequestDto dto) {
        Exhibition exhibition = findExhibitionWithJoinById(exhibitionId);

        exhibition.update(dto);

        if (dto.artists() != null) {
            exhibitionArtistMappingHelper.updateExhibitionArtists(dto, exhibition);
        }

        return ExhibitionUpdateResponseDto.from(exhibition);
    }

    @Transactional
    public ExhibitionUpdateActivationResponseDto updateExhibitionActivation(Long exhibitionId) {
        Exhibition exhibition = findExhibitionById(exhibitionId);
        exhibition.changeActivation();

        return ExhibitionUpdateActivationResponseDto.from(exhibition);
    }

    private Exhibition findExhibitionWithJoinById(Long exhibitionId) {
        return exhibitionQuerydslRepository.findExhibitionById(exhibitionId)
                .orElseThrow(() -> new CustomRuntimeException(ExhibitionExceptionType.EXHIBITION_NOT_FOUND));
    }

    private Exhibition findExhibitionById(Long exhibitionId) {
        return exhibitionJpaRepository.findById(exhibitionId)
                .orElseThrow(() -> new CustomRuntimeException(ExhibitionExceptionType.EXHIBITION_NOT_FOUND));
    }
}
