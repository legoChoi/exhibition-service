package com.dayaeyak.exhibition.domain.exhibition.application;

import com.dayaeyak.exhibition.domain.artist.Artist;
import com.dayaeyak.exhibition.domain.artist.jpa.ArtistJpaRepository;
import com.dayaeyak.exhibition.domain.exhibition.domain.Exhibition;
import com.dayaeyak.exhibition.domain.exhibition.presentation.dto.request.ExhibitionUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class ExhibitionArtistMappingHelper {

    private final ArtistJpaRepository artistJpaRepository;

    public void updateExhibitionArtists(ExhibitionUpdateRequestDto dto, Exhibition exhibition) {
        Set<String> requestedArtistNames = dto.getUpdateArtistNameSet();
        Set<String> currentArtistNames = exhibition.getExhibitionArtistNameSet();

        // 추가 할 아티스트
        Set<String> addArtistNames = buildExcludedNameSet(requestedArtistNames, currentArtistNames);
        // 삭제 할 아티스트
        Set<String> deleteArtistNames = buildExcludedNameSet(currentArtistNames, requestedArtistNames);

        exhibition.deleteExhibitionArtistsByNames(deleteArtistNames);

        if (!addArtistNames.isEmpty()) {
            List<Artist> newArtists = findOrCreateArtists(addArtistNames);
            exhibition.addExhibitionArtists(newArtists);
        }
    }

    /**
     * 조회 후 없으면 생성 후 아티스트 리스트 반환
     */
    public List<Artist> findOrCreateArtists(Set<String> artistNames) {
        List<Artist> foundArtists = artistJpaRepository.findByNameInAndDeletedAtIsNull(artistNames);

        Set<String> existingArtists = foundArtists.stream()
                .map(Artist::getName)
                .collect(Collectors.toSet());

        List<Artist> newArtists = artistNames.stream()
                .filter(name -> !existingArtists.contains(name))
                .map(Artist::new)
                .toList();

        if (!newArtists.isEmpty()) {
            artistJpaRepository.saveAll(newArtists);
        }

        return Stream.concat(foundArtists.stream(), newArtists.stream())
                .toList();
    }

    private Set<String> buildExcludedNameSet(Set<String> base, Set<String> exclude) {
        Set<String> set = new HashSet<>(base);
        set.removeAll(exclude);
        return set;
    }
}
