package com.dayaeyak.exhibition.domain.artist.application;

import com.dayaeyak.exhibition.common.exception.CustomRuntimeException;
import com.dayaeyak.exhibition.common.exception.type.ArtistExceptionType;
import com.dayaeyak.exhibition.domain.artist.domain.Artist;
import com.dayaeyak.exhibition.domain.artist.presentation.dto.request.ArtistCreateRequestDto;
import com.dayaeyak.exhibition.domain.artist.presentation.dto.request.ArtistUpdateRequestDto;
import com.dayaeyak.exhibition.domain.artist.presentation.dto.response.ArtistCreateResponseDto;
import com.dayaeyak.exhibition.domain.artist.presentation.dto.response.ArtistSearchPageResponseDto;
import com.dayaeyak.exhibition.domain.artist.presentation.dto.response.ArtistUpdateResponseDto;
import com.dayaeyak.exhibition.domain.artist.infrastructure.jpa.ArtistJpaRepository;
import com.dayaeyak.exhibition.domain.artist.infrastructure.querydsl.ArtistQuerydslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArtistService {

    private final ArtistJpaRepository artistJpaRepository;
    private final ArtistQuerydslRepository artistQuerydslRepository;

    public ArtistCreateResponseDto createArtist(ArtistCreateRequestDto dto) {
        if (artistJpaRepository.existsByNameAndDeletedAtIsNull(dto.name())) {
            throw new CustomRuntimeException(ArtistExceptionType.ALREADY_REGISTERED_ARTIST);
        }

        Artist artist = new Artist(dto.name());

        Artist createdArtist = artistJpaRepository.save(artist);

        return ArtistCreateResponseDto.from(createdArtist);
    }

    public ArtistSearchPageResponseDto searchArtistPage(int page, int size, String name) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Artist> data = artistQuerydslRepository.search(pageable, name);

        return ArtistSearchPageResponseDto.from(data);
    }

    @Transactional
    public ArtistUpdateResponseDto updateArtist(Long artistId, ArtistUpdateRequestDto dto) {
        Artist artist = findArtistById(artistId);
        artist.update(dto);

        return ArtistUpdateResponseDto.from(artist);
    }

    @Transactional
    public void deleteArtist(Long artistId) {
        Artist artist = findArtistById(artistId);
        artist.delete();
    }

    private Artist findArtistById(Long artistId) {
        return artistJpaRepository.findByIdAndDeletedAtIsNull(artistId)
                .orElseThrow(() -> new CustomRuntimeException(ArtistExceptionType.ARTIST_NOT_FOUND));
    }
}
