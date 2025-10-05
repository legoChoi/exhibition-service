package com.dayaeyak.exhibition.domain.exhibition.infrastructure.querydsl;

import com.dayaeyak.exhibition.domain.exhibition.domain.Exhibition;
import com.dayaeyak.exhibition.domain.exhibition.domain.enums.Grade;
import com.dayaeyak.exhibition.domain.exhibition.domain.enums.Region;
import com.dayaeyak.exhibition.domain.exhibition.domain.enums.SearchType;
import com.dayaeyak.exhibition.domain.exhibition.infrastructure.querydsl.dto.response.ExhibitionFindArtistProjectionDto;
import com.dayaeyak.exhibition.domain.exhibition.infrastructure.querydsl.dto.response.ExhibitionFindBookingDataProjectionDto;
import com.dayaeyak.exhibition.domain.exhibition.infrastructure.querydsl.dto.response.ExhibitionFindProjectionDto;
import com.dayaeyak.exhibition.domain.exhibition.infrastructure.querydsl.dto.response.ExhibitionSearchProjectionDto;
import com.dayaeyak.exhibition.domain.exhibition.querydsl.dto.response.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dayaeyak.exhibition.domain.artist.QArtist.artist;
import static com.dayaeyak.exhibition.domain.exhibition.QExhibition.exhibition;
import static com.dayaeyak.exhibition.domain.exhibition.QExhibitionArtist.exhibitionArtist;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Repository
@RequiredArgsConstructor
public class ExhibitionQuerydslRepositoryImpl implements ExhibitionQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<ExhibitionFindProjectionDto> findExhibitionDetail(Long exhibitionId) {
        Map<Long, ExhibitionFindProjectionDto> record = queryFactory
                .selectFrom(exhibition)
                .innerJoin(exhibition.artistList, exhibitionArtist)
                .innerJoin(exhibitionArtist.artist, artist)
                .where(
                        exhibition.id.eq(exhibitionId),
                        exhibition.isActivated.eq(true),
                        exhibition.deletedAt.isNull()
                )
                .transform(groupBy(exhibition.id).as(
                        new QExhibitionFindProjectionDto(
                                exhibition.id,
                                exhibition.price,
                                exhibition.name,
                                exhibition.place,
                                exhibition.address,
                                exhibition.region,
                                exhibition.grade,
                                exhibition.startDate,
                                exhibition.endDate,
                                exhibition.startTime,
                                exhibition.endTime,
                                exhibition.ticketOpenedAt,
                                exhibition.ticketClosedAt,
                                exhibition.isActivated,
                                list(Projections.constructor(ExhibitionFindArtistProjectionDto.class,
                                        artist.id,
                                        artist.name
                                ))
                        )
                ));

        return Optional.ofNullable(record.get(exhibitionId));
    }

    @Override
    public Optional<Exhibition> findExhibitionById(Long exhibitionId) {
        Exhibition record = queryFactory.selectFrom(exhibition)
                .innerJoin(exhibition.artistList, exhibitionArtist).fetchJoin()
                .innerJoin(exhibitionArtist.artist, artist).fetchJoin()
                .where(
                        exhibition.id.eq(exhibitionId),
                        exhibition.deletedAt.isNull()
                )
                .fetchOne();

        return Optional.ofNullable(record);
    }

    @Override
    public Page<ExhibitionSearchProjectionDto> searchExhibitions(
            Pageable pageable,
            Region region,
            Grade grade,
            LocalDate startDate,
            LocalDate endDate,
            String keyword,
            SearchType searchType
    ) {
        boolean isArtistSearch = isArtistSearch(searchType, keyword);

        Predicate[] whereClauses = {
                eqSearchType(searchType, keyword),
                eqRegion(region),
                eqGrade(grade),
                buildDateSearchCondition(startDate, endDate),
                exhibition.isActivated.eq(true),
                exhibition.deletedAt.isNull()
        };

        JPAQuery<ExhibitionSearchProjectionDto> query = createSearchQuery(isArtistSearch);

        applyArtistJoin(query, isArtistSearch);

        List<ExhibitionSearchProjectionDto> records = query
                .where(whereClauses)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = createSearchCountQuery(isArtistSearch)
                .where(whereClauses);

        applyArtistJoin(countQuery, isArtistSearch);

        return PageableExecutionUtils.getPage(records, pageable, countQuery::fetchOne);
    }

    @Override
    public Optional<ExhibitionFindBookingDataProjectionDto> findExhibitionForBooking(Long exhibitionId) {
        ExhibitionFindBookingDataProjectionDto record = queryFactory.select(new QExhibitionFindBookingDataProjectionDto(
                        exhibition.price,
                        exhibition.grade
                ))
                .from(exhibition)
                .where(
                        exhibition.id.eq(exhibitionId),
                        exhibition.deletedAt.isNull()
                )
                .fetchOne();

        return Optional.ofNullable(record);
    }

    private JPAQuery<ExhibitionSearchProjectionDto> createSearchQuery(boolean isArtistSearch) {
        QExhibitionSearchProjectionDto projection = new QExhibitionSearchProjectionDto(
                exhibition.id,
                exhibition.name,
                exhibition.place,
                exhibition.region,
                exhibition.grade,
                exhibition.startDate,
                exhibition.endDate,
                exhibition.startTime,
                exhibition.endTime,
                exhibition.ticketOpenedAt,
                exhibition.ticketClosedAt
        );

        if (isArtistSearch) {
            return queryFactory
                    .selectDistinct(projection)
                    .from(exhibition);
        }

        return queryFactory
                .select(projection)
                .from(exhibition);
    }

    private JPAQuery<Long> createSearchCountQuery(boolean isArtistSearch) {
        if (isArtistSearch) {
            return queryFactory
                    .selectDistinct(exhibition.count())
                    .from(exhibition);
        }

        return queryFactory
                .select(exhibition.count())
                .from(exhibition);
    }

    private boolean isArtistSearch(SearchType searchType, String keyword) {
        return searchType == SearchType.ARTIST && StringUtils.hasText(keyword);
    }

    private BooleanBuilder buildDateSearchCondition(LocalDate startDate, LocalDate endDate) {
        BooleanBuilder dateRangeBuilder = new BooleanBuilder();

        /*
            startDate만 존재하는 경우 -> startDate 이후에 시작되거나 진행중인 전시회 목록 출력

            ~ AND (
                exhibition.startDate <= :startDate AND exhibition.endDate >= :startDate
            )
         */
        if (startDate != null && endDate == null) {
            dateRangeBuilder
                    .and(
                            exhibition.startDate.loe(startDate)
                                    .and(exhibition.endDate.goe(startDate))
                    );
        }

        /*
            startDate & endDate 둘 다 존재하는 경우
            1. exhibition.startDate :startDate :endDate exhibition.endDate
            2. exhibition.startDate :startDate exhibition.endDate :endDate
            3. :startDate exhibition.startDate :endDate exhibition.endDate

            ~ AND (
                exhibition.startDate <= :endDate AND exhibition.endDate >= :startDate
            )
         */
        if (startDate != null && endDate != null) {
            dateRangeBuilder
                    .and(exhibition.startDate.loe(endDate)
                            .and(exhibition.endDate.goe(startDate))
                    );
        }

        return dateRangeBuilder;
    }

    private <T> void applyArtistJoin(JPAQuery<T> query, boolean isArtistSearch) {
        if (isArtistSearch) {
            query
                    .innerJoin(exhibition.artistList, exhibitionArtist)
                    .innerJoin(exhibitionArtist.artist, artist);
        }
    }

    private BooleanExpression eqRegion(Region region) {
        return region != null ? exhibition.region.eq(region) : null;
    }

    private BooleanExpression eqGrade(Grade grade) {
        return grade != null ? exhibition.grade.eq(grade) : null;
    }

    private BooleanExpression eqSearchType(SearchType searchType, String keyword) {
        if (searchType == null || !StringUtils.hasText(keyword)) {
            return null;
        }

//        return switch (searchType) {
//            case NAME -> exhibition.name.contains(keyword);
//            case PLACE -> exhibition.place.contains(keyword);
//            case ARTIST -> artist.name.contains(keyword);
//        };

        return switch (searchType) {
            case NAME -> Expressions.booleanTemplate(
                    "FUNCTION('match_against', {0}, {1}) > 0", exhibition.name, keyword);
            case PLACE -> Expressions.booleanTemplate(
                    "FUNCTION('match_against', {0}, {1}) > 0", exhibition.place, keyword);
            case ARTIST -> Expressions.booleanTemplate(
                    "FUNCTION('match_against', {0}, {1}) > 0", artist.name, keyword);
        };
    }
}
