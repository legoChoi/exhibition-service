package com.dayaeyak.exhibition.domain.exhibition.domain;

import com.dayaeyak.exhibition.common.entity.BaseEntity;
import com.dayaeyak.exhibition.common.exception.CustomRuntimeException;
import com.dayaeyak.exhibition.common.exception.type.ExhibitionExceptionType;
import com.dayaeyak.exhibition.domain.artist.domain.Artist;
import com.dayaeyak.exhibition.domain.exhibition.presentation.dto.request.ExhibitionUpdateRequestDto;
import com.dayaeyak.exhibition.domain.exhibition.domain.enums.Grade;
import com.dayaeyak.exhibition.domain.exhibition.domain.enums.Region;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(
        name = "exhibitions",
        indexes = {
                @Index(name = "idx_seller_id", columnList = "seller_id"),
                @Index(name = "idx_composite_start_end_date", columnList = "start_date, end_date")
        }
)

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Exhibition extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exhibition_id")
    private Long id;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Long sellerId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String place;

    @Column(nullable = false, length = 100)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Region region;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Grade grade;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private LocalDateTime ticketOpenedAt;

    @Column(nullable = false)
    private LocalDateTime ticketClosedAt;

    @Column(nullable = false)
    @ColumnDefault("true")
    private Boolean isActivated = true;

    @OneToMany(mappedBy = "exhibition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExhibitionArtist> artistList = new ArrayList<>();

    @Builder
    public Exhibition(
            Integer price,
            Long sellerId,
            String name,
            String place,
            String address,
            Region region,
            Grade grade,
            LocalDate startDate,
            LocalDate endDate,
            LocalTime startTime,
            LocalTime endTime,
            LocalDateTime ticketOpenedAt,
            LocalDateTime ticketClosedAt
    ) {
        this.price = price;
        this.sellerId = sellerId;
        this.name = name;
        this.place = place;
        this.address = address;
        this.region = region;
        this.grade = grade;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.ticketOpenedAt = ticketOpenedAt;
        this.ticketClosedAt = ticketClosedAt;
    }

    public void deleteExhibitionArtistsByNames(Set<String> requestedArtistNames) {
        this.artistList.removeIf(exhibitionArtist ->
                requestedArtistNames.contains(exhibitionArtist.getArtist().getName()));
    }

    public void addExhibitionArtists(List<Artist> artists) {
        artists.forEach(artist -> this.addExhibitionArtist(new ExhibitionArtist(artist)));
    }

    public void addExhibitionArtist(ExhibitionArtist exhibitionArtist) {
        artistList.add(exhibitionArtist);
        exhibitionArtist.linkExhibition(this);
    }

    public void update(ExhibitionUpdateRequestDto dto) {
        updatePrice(dto.price());
        updateName(dto.name());
        updatePlace(dto.place());
        updateAddress(dto.address());
        updateRegion(dto.region());
        updateGrade(dto.grade());
        updateStartDate(dto.startDate());
        updateEndDate(dto.endDate());
        updateStartTime(dto.startTime());
        updateEndTime(dto.endTime());
        updateTicketOpenedAt(dto.ticketOpenedAt());
        updateTicketClosedAt(dto.ticketClosedAt());
    }

    public Set<String> getExhibitionArtistNameSet() {
        return this.getArtistList().stream()
                .map(ExhibitionArtist::getArtist)
                .map(Artist::getName)
                .collect(Collectors.toSet());
    }

    private void updatePrice(Integer price) {
        if (price != null) {
            this.price = price;
        }
    }

    private void updateName(String name) {
        if (StringUtils.hasText(name)) {
            this.name = name;
        }
    }

    private void updatePlace(String place) {
        if (StringUtils.hasText(place)) {
            this.place = place;
        }
    }

    private void updateAddress(String address) {
        if (StringUtils.hasText(address)) {
            this.address = address;
        }
    }

    private void updateRegion(Region region) {
        if (region != null) {
            this.region = region;
        }
    }

    private void updateGrade(Grade grade) {
        if (grade != null) {
            this.grade = grade;
        }
    }

    private void updateStartDate(LocalDate startDate) {
        if (startDate != null) {
            this.startDate = startDate;
        }
    }

    private void updateEndDate(LocalDate endDate) {
        if (endDate == null) {
            return;
        }

        if (this.startDate.isAfter(endDate)) {
            throw new CustomRuntimeException(ExhibitionExceptionType.END_DATE_BEFORE_START_DATE);
        }

        this.endDate = endDate;
    }

    private void updateStartTime(LocalTime startTime) {
        if (startTime != null) {
            this.startTime = startTime;
        }
    }

    private void updateEndTime(LocalTime endTime) {
        if (endTime == null) {
            return;
        }

        if (this.startTime.isAfter(endTime)) {
            throw new CustomRuntimeException(ExhibitionExceptionType.END_TIME_BEFORE_START_TIME);
        }

        this.endTime = endTime;
    }

    private void updateTicketOpenedAt(LocalDateTime ticketOpenedAt) {
        if (ticketOpenedAt != null) {
            this.ticketOpenedAt = ticketOpenedAt;
        }
    }

    private void updateTicketClosedAt(LocalDateTime ticketClosedAt) {
        if (ticketClosedAt == null) {
            return;
        }

        if (this.ticketOpenedAt.isAfter(ticketClosedAt)) {
            throw new CustomRuntimeException(ExhibitionExceptionType.TICKET_CLOSE_DATE_TIME_BEFORE_OPEN_DATE_TIME);
        }

        this.ticketClosedAt = ticketClosedAt;
    }

    public void changeActivation() {
        this.isActivated = !this.isActivated;
    }
}
