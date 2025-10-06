package com.dayaeyak.exhibition.domain.artist.infrastructure.querydsl;

import com.dayaeyak.exhibition.domain.artist.domain.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArtistQuerydslRepository {

    Page<Artist> search(Pageable pageable, String name);
}
