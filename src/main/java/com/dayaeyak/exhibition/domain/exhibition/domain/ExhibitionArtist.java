package com.dayaeyak.exhibition.domain.exhibition.domain;

import com.dayaeyak.exhibition.domain.artist.domain.Artist;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "exhibition_artists")

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExhibitionArtist {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exhibition_artist_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibition_id")
    private Exhibition exhibition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    private Artist artist;

    public ExhibitionArtist(Artist artist) {
        this.artist = artist;
    }

    public void linkExhibition(Exhibition exhibition) {
        this.exhibition = exhibition;
    }
}
