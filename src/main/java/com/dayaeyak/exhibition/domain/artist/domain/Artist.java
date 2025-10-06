package com.dayaeyak.exhibition.domain.artist.domain;

import com.dayaeyak.exhibition.common.entity.BaseEntity;
import com.dayaeyak.exhibition.domain.artist.presentation.dto.request.ArtistUpdateRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Entity
@Table(name = "artists")

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Artist extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artist_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String name;

    public Artist(String name) {
        this.name = name;
    }

    public void update(ArtistUpdateRequestDto dto) {
        updateName(dto.name());
    }

    private void updateName(String name) {
        if (StringUtils.hasText(name)) {
            this.name = name;
        }
    }
}
