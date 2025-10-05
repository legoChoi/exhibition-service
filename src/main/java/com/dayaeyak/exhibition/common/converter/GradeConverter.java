package com.dayaeyak.exhibition.common.converter;

import com.dayaeyak.exhibition.domain.exhibition.domain.enums.Grade;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GradeConverter implements Converter<String, Grade> {

    @Override
    public Grade convert(String source) {
        return Grade.of(source);
    }
}
