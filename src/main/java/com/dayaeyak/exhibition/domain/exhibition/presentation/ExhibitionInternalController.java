package com.dayaeyak.exhibition.domain.exhibition.presentation;

import com.dayaeyak.exhibition.domain.exhibition.application.ExhibitionInternalService;
import com.dayaeyak.exhibition.domain.exhibition.presentation.dto.request.ExhibitionCreateRequestDto;
import com.dayaeyak.exhibition.domain.exhibition.presentation.dto.response.ExhibitionBookingDataResponseDto;
import com.dayaeyak.exhibition.domain.exhibition.presentation.dto.response.ExhibitionCreateResponseDto;
import com.dayaeyak.exhibition.domain.exhibition.presentation.dto.response.ExhibitionSearchPageResponseDto;
import com.dayaeyak.exhibition.domain.exhibition.domain.enums.Grade;
import com.dayaeyak.exhibition.domain.exhibition.domain.enums.Region;
import com.dayaeyak.exhibition.domain.exhibition.domain.enums.SearchType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/internal/exhibitions")
@RequiredArgsConstructor
public class ExhibitionInternalController {

    private final ExhibitionInternalService exhibitionInternalService;

    @PostMapping
    public ExhibitionCreateResponseDto createExhibition(
            @RequestBody ExhibitionCreateRequestDto exhibitionCreateRequestDto
    ) {
        return exhibitionInternalService.createExhibition(exhibitionCreateRequestDto);
    }

    @GetMapping
    public ExhibitionSearchPageResponseDto searchExhibition(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Region region,
            @RequestParam(required = false) Grade grade,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) SearchType searchType
    ) {
        return exhibitionInternalService.searchExhibition(page, size, region, grade, startDate, endDate, keyword, searchType);
    }

    @GetMapping("/{exhibitionId}/booking-details")
    public ExhibitionBookingDataResponseDto getExhibitionBookingData(
            @PathVariable Long exhibitionId
    ) {
        return exhibitionInternalService.findExhibitionOrderData(exhibitionId);
    }
}
