package com.dayaeyak.exhibition.domain.artist.presentation;

import com.dayaeyak.exhibition.common.entity.ApiResponse;
import com.dayaeyak.exhibition.domain.artist.application.ArtistService;
import com.dayaeyak.exhibition.common.constraints.ArtistResponseMessage;
import com.dayaeyak.exhibition.domain.artist.presentation.dto.request.ArtistCreateRequestDto;
import com.dayaeyak.exhibition.domain.artist.presentation.dto.request.ArtistUpdateRequestDto;
import com.dayaeyak.exhibition.domain.artist.presentation.dto.response.ArtistCreateResponseDto;
import com.dayaeyak.exhibition.domain.artist.presentation.dto.response.ArtistSearchPageResponseDto;
import com.dayaeyak.exhibition.domain.artist.presentation.dto.response.ArtistUpdateResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/artists")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    @PostMapping
    public ResponseEntity<ApiResponse<ArtistCreateResponseDto>> createArtist(
            @RequestBody @Valid ArtistCreateRequestDto artistCreateRequestDto
    ) {
        ArtistCreateResponseDto data = artistService.createArtist(artistCreateRequestDto);

        return ApiResponse.success(HttpStatus.CREATED, ArtistResponseMessage.ARTIST_CREATE_SUCCESS, data);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<ArtistSearchPageResponseDto>> searchArtist(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name
    ) {
        ArtistSearchPageResponseDto data = artistService.searchArtistPage(page, size, name);

        return ApiResponse.success(HttpStatus.OK, ArtistResponseMessage.ARTIST_SEARCH_SUCCESS, data);
    }

    @PatchMapping("/{artistId}")
    public ResponseEntity<ApiResponse<ArtistUpdateResponseDto>> updateArtist(
            @PathVariable Long artistId,
            @RequestBody @Valid ArtistUpdateRequestDto artistUpdateRequestDto
    ) {
        ArtistUpdateResponseDto data = artistService.updateArtist(artistId, artistUpdateRequestDto);

        return ApiResponse.success(HttpStatus.OK, ArtistResponseMessage.ARTIST_UPDATE_SUCCESS, data);
    }

    @DeleteMapping("/{artistId}")
    public ResponseEntity<ApiResponse<Void>> deleteArtist(
            @PathVariable Long artistId
    ) {
        artistService.deleteArtist(artistId);

        return ApiResponse.success(HttpStatus.OK, ArtistResponseMessage.ARTIST_DELETE_SUCCESS);
    }
}
