package com.dayaeyak.exhibition.common.converter;

import com.dayaeyak.exhibition.domain.exhibition.domain.enums.Region;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RegionConverter implements Converter<String, Region> {

    @Override
    public Region convert(String source) {
        return Region.of(source);
    }
}
