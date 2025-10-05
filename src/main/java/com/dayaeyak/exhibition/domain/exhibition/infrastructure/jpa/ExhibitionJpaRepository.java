package com.dayaeyak.exhibition.domain.exhibition.infrastructure.jpa;

import com.dayaeyak.exhibition.domain.exhibition.domain.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExhibitionJpaRepository extends JpaRepository<Exhibition, Long> {
}
