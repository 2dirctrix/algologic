package com.ssafy.c204_be_api.problem.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.c204_be_api.problem.domain.ProblemExample;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Converter
@RequiredArgsConstructor
public class ProblemExampleConverter implements AttributeConverter<List<ProblemExample>, String> {

    private final ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(final List<ProblemExample> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("ProblemExample DB Column 으로 변환 중 예외 발생", e);
        }
    }

    @Override
    public List<ProblemExample> convertToEntityAttribute(final String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<ProblemExample>>() {});
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("ProblemExample 객체로 변환 중 예외 발생", e);
        }
    }
}
