package com.dayaeyak.exhibition.domain.exhibition.domain.enums;

import com.dayaeyak.exhibition.common.exception.CustomRuntimeException;
import com.dayaeyak.exhibition.common.exception.type.ExhibitionExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum SearchType {

    NAME("name"),
    PLACE("place"),
    ARTIST("artist"),
    ;

    private final String type;

    public static SearchType of(String type) {
        return Arrays.stream(SearchType.values())
                .filter(searchType -> searchType.type.equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(() -> new CustomRuntimeException(ExhibitionExceptionType.INVALID_SEARCH_TYPE));
    }
}
