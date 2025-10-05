package com.dayaeyak.exhibition.domain.exhibition.infrastructure.jpa;

import com.dayaeyak.exhibition.domain.exhibition.domain.ExhibitionArtist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExhibitionArtistJpaRepository extends JpaRepository<ExhibitionArtist, Long> {
}
