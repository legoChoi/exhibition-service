package com.dayaeyak.exhibition.domain.artist.infrastructure.querydsl;

import com.dayaeyak.exhibition.domain.artist.domain.Artist;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.dayaeyak.exhibition.domain.artist.QArtist.artist;

@Repository
@RequiredArgsConstructor
public class ArtistQuerydslRepositoryImpl implements ArtistQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Artist> search(Pageable pageable, String name) {
        Predicate[] whereClauses = {
                eqName(name),
                artist.deletedAt.isNull()
        };

        List<Artist> data = queryFactory.select(artist)
                .from(artist)
                .where(whereClauses)
                .orderBy(artist.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = queryFactory.select(artist.count())
                .from(artist)
                .where(whereClauses);

        return PageableExecutionUtils.getPage(data, pageable, count::fetchOne);
    }

    private BooleanExpression eqName(String name) {
        return StringUtils.hasText(name) ? artist.name.contains(name) : null;
    }
}
