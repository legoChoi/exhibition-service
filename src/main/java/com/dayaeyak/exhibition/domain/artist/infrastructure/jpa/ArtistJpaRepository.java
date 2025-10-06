package com.dayaeyak.exhibition.domain.artist.infrastructure.jpa;

import com.dayaeyak.exhibition.domain.artist.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ArtistJpaRepository extends JpaRepository<Artist, Long> {

    boolean existsByNameAndDeletedAtIsNull(String name);

    Optional<Artist> findByIdAndDeletedAtIsNull(Long id);

    List<Artist> findByNameInAndDeletedAtIsNull(Collection<String> names);
}
