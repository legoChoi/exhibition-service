package com.dayaeyak.exhibition.domain.artist.presentation.dto.request;

import com.dayaeyak.exhibition.common.constraints.ArtistValidationMessage;
import jakarta.validation.constraints.NotBlank;

public record ArtistCreateRequestDto(

        @NotBlank(message = ArtistValidationMessage.INVALID_NAME_MESSAGE)
//        @Size(
//                min = ArtistValidationMessage.NAME_MIN_LENGTH,
//                max = ArtistValidationMessage.NAME_MAX_LENGTH,
//                message = ArtistValidationMessage.INVALID_NAME_MESSAGE
//        )
        String name
) {
}
