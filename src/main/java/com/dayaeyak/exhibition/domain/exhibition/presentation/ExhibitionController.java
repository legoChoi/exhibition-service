package com.dayaeyak.exhibition.domain.exhibition.presentation;

import com.dayaeyak.exhibition.common.annotation.Authorize;
import com.dayaeyak.exhibition.common.annotation.PassportHolder;
import com.dayaeyak.exhibition.common.constraints.ExhibitionResponseMessage;
import com.dayaeyak.exhibition.common.dto.Passport;
import com.dayaeyak.exhibition.common.entity.ApiResponse;
import com.dayaeyak.exhibition.common.enums.UserRole;
import com.dayaeyak.exhibition.domain.exhibition.application.ExhibitionService;
import com.dayaeyak.exhibition.domain.exhibition.presentation.dto.request.ExhibitionUpdateRequestDto;
import com.dayaeyak.exhibition.domain.exhibition.presentation.dto.response.ExhibitionFindResponseDto;
import com.dayaeyak.exhibition.domain.exhibition.presentation.dto.response.ExhibitionUpdateActivationResponseDto;
import com.dayaeyak.exhibition.domain.exhibition.presentation.dto.response.ExhibitionUpdateResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exhibitions")
@RequiredArgsConstructor
public class ExhibitionController {

    private final ExhibitionService exhibitionService;

    @GetMapping("/{exhibitionId}")
    public ResponseEntity<ApiResponse<ExhibitionFindResponseDto>> getExhibition(
            @PathVariable Long exhibitionId
    ) {
        ExhibitionFindResponseDto data = exhibitionService.findExhibition(exhibitionId);

        return ApiResponse.success(HttpStatus.OK, ExhibitionResponseMessage.FIND, data);
    }

    @PatchMapping("/{exhibitionId}")
    @Authorize(roles = { UserRole.SELLER })
    public ResponseEntity<ApiResponse<ExhibitionUpdateResponseDto>> updateExhibition(
            @PathVariable Long exhibitionId,
            @RequestBody ExhibitionUpdateRequestDto exhibitionUpdateRequestDto,
            @PassportHolder Passport passport
    ) {
        ExhibitionUpdateResponseDto data = exhibitionService.updateExhibition(exhibitionId, exhibitionUpdateRequestDto, passport);

        return ApiResponse.success(HttpStatus.OK, ExhibitionResponseMessage.UPDATE, data);
    }

    @PatchMapping("/{exhibitionId}/activation")
    @Authorize(roles = { UserRole.SELLER })
    public ResponseEntity<ApiResponse<ExhibitionUpdateActivationResponseDto>> updateExhibitionActivation(
            @PathVariable Long exhibitionId,
            @PassportHolder Passport passport
    ) {
        ExhibitionUpdateActivationResponseDto data = exhibitionService.updateExhibitionActivation(exhibitionId, passport);

        return ApiResponse.success(HttpStatus.OK, ExhibitionResponseMessage.ACTIVATION_UPDATE, data);
    }
}
