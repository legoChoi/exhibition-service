package com.dayaeyak.exhibition.domain.exhibition.application;

import com.dayaeyak.exhibition.common.dto.Passport;
import com.dayaeyak.exhibition.common.exception.CustomRuntimeException;
import com.dayaeyak.exhibition.common.exception.type.ExhibitionExceptionType;
import com.dayaeyak.exhibition.domain.exhibition.domain.Exhibition;
import com.dayaeyak.exhibition.domain.exhibition.presentation.dto.request.ExhibitionUpdateRequestDto;
import com.dayaeyak.exhibition.domain.exhibition.presentation.dto.response.ExhibitionFindResponseDto;
import com.dayaeyak.exhibition.domain.exhibition.presentation.dto.response.ExhibitionUpdateActivationResponseDto;
import com.dayaeyak.exhibition.domain.exhibition.presentation.dto.response.ExhibitionUpdateResponseDto;
import com.dayaeyak.exhibition.domain.exhibition.infrastructure.jpa.ExhibitionJpaRepository;
import com.dayaeyak.exhibition.domain.exhibition.infrastructure.querydsl.ExhibitionQuerydslRepository;
import com.dayaeyak.exhibition.domain.exhibition.infrastructure.querydsl.dto.response.ExhibitionFindProjectionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ExhibitionService {

    private final ExhibitionQuerydslRepository exhibitionQuerydslRepository;
    private final ExhibitionJpaRepository exhibitionJpaRepository;

    private final ExhibitionArtistMappingHelper exhibitionArtistMappingHelper;

    public ExhibitionFindResponseDto findExhibition(Long exhibitionId) {
        ExhibitionFindProjectionDto record = exhibitionQuerydslRepository.findExhibitionDetail(exhibitionId)
                .orElseThrow(() -> new CustomRuntimeException(ExhibitionExceptionType.EXHIBITION_NOT_FOUND));

        return ExhibitionFindResponseDto.from(record);
    }

    @Transactional
    public ExhibitionUpdateResponseDto updateExhibition(Long exhibitionId, ExhibitionUpdateRequestDto dto, Passport passport) {
        Exhibition exhibition = findExhibitionWithJoinById(exhibitionId);

        // validations
        validateOwnExhibition(passport.userId(), exhibition.getSellerId());
        validateTicketOpenTime(exhibition.getTicketOpenedAt());

        exhibition.update(dto);

        if (dto.artists() != null) {
            exhibitionArtistMappingHelper.updateExhibitionArtists(dto, exhibition);
        }

        return ExhibitionUpdateResponseDto.from(exhibition);
    }

    @Transactional
    public ExhibitionUpdateActivationResponseDto updateExhibitionActivation(Long exhibitionId, Passport passport) {
        Exhibition exhibition = findExhibitionById(exhibitionId);

        // validations
        validateOwnExhibition(passport.userId(), exhibition.getSellerId());
        validateTicketOpenTime(exhibition.getTicketOpenedAt());

        exhibition.changeActivation();

        return ExhibitionUpdateActivationResponseDto.from(exhibition);
    }

    private void validateTicketOpenTime(LocalDateTime ticketOpenAt) {
        if (ticketOpenAt.isBefore(LocalDateTime.now())) {
            throw new CustomRuntimeException(ExhibitionExceptionType.ALREADY_EXHIBITION_TICKET_OPENED);
        }
    }

    private void validateOwnExhibition(Long requestUserId, Long ownerId) {
        if (!requestUserId.equals(ownerId)) {
            throw new CustomRuntimeException(ExhibitionExceptionType.ACCESS_DENIED);
        }
    }

    private Exhibition findExhibitionById(Long exhibitionId) {
        return exhibitionJpaRepository.findById(exhibitionId)
                .orElseThrow(() -> new CustomRuntimeException(ExhibitionExceptionType.EXHIBITION_NOT_FOUND));
    }

    private Exhibition findExhibitionWithJoinById(Long exhibitionId) {
        return exhibitionQuerydslRepository.findExhibitionById(exhibitionId)
                .orElseThrow(() -> new CustomRuntimeException(ExhibitionExceptionType.EXHIBITION_NOT_FOUND));
    }
}
