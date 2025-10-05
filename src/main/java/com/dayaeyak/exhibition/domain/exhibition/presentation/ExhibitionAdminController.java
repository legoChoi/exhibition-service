package com.dayaeyak.exhibition.domain.exhibition.presentation;

import com.dayaeyak.exhibition.common.constraints.ExhibitionResponseMessage;
import com.dayaeyak.exhibition.common.entity.ApiResponse;
import com.dayaeyak.exhibition.domain.exhibition.application.ExhibitionAdminService;
import com.dayaeyak.exhibition.domain.exhibition.presentation.dto.request.ExhibitionUpdateRequestDto;
import com.dayaeyak.exhibition.domain.exhibition.presentation.dto.response.ExhibitionUpdateActivationResponseDto;
import com.dayaeyak.exhibition.domain.exhibition.presentation.dto.response.ExhibitionUpdateResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/exhibitions")
@RequiredArgsConstructor
public class ExhibitionAdminController {

    public final ExhibitionAdminService exhibitionAdminService;

    @PatchMapping("/{exhibitionId}")
    public ResponseEntity<ApiResponse<ExhibitionUpdateResponseDto>> updateExhibition(
            @PathVariable Long exhibitionId,
            @RequestBody ExhibitionUpdateRequestDto exhibitionUpdateRequestDto
    ) {
        ExhibitionUpdateResponseDto data = exhibitionAdminService.updateExhibition(exhibitionId, exhibitionUpdateRequestDto);
        return ApiResponse.success(HttpStatus.OK, ExhibitionResponseMessage.UPDATE, data);
    }

    @PatchMapping("/{exhibitionId}/activation")
    public ResponseEntity<ApiResponse<ExhibitionUpdateActivationResponseDto>> updateExhibitionActivation(
            @PathVariable Long exhibitionId
    ) {
        ExhibitionUpdateActivationResponseDto data = exhibitionAdminService.updateExhibitionActivation(exhibitionId);
        return ApiResponse.success(HttpStatus.OK, ExhibitionResponseMessage.ACTIVATION_UPDATE, data);
    }
}
