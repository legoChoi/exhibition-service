package com.dayaeyak.exhibition.common.converter;

import com.dayaeyak.exhibition.domain.exhibition.domain.enums.SearchType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SearchTypeConverter implements Converter<String, SearchType> {

    @Override
    public SearchType convert(String source) {
        return SearchType.of(source);
    }
}
