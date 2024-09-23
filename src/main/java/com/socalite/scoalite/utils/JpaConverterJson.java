package com.socalite.scoalite.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socalite.scoalite.post.model.ReactionCounts;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter(autoApply = true)
public class JpaConverterJson implements AttributeConverter<ReactionCounts, String> {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(ReactionCounts meta) {
        try {
            return objectMapper.writeValueAsString(meta);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ReactionCounts convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, ReactionCounts.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}