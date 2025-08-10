package com.example.cms.util;

import com.example.cms.entity.Skill;
import com.example.cms.entity.Strength;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

public class JsonConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Converter
    public static class StrengthConverter implements AttributeConverter<List<Strength>, String> {
        @Override
        public String convertToDatabaseColumn(List<Strength> attribute) {
            try {
                return objectMapper.writeValueAsString(attribute);
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("Error converting List<Strength> to JSON", e);
            }
        }

        @Override
        public List<Strength> convertToEntityAttribute(String json) {
            try {
                return json != null
                        ? objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, Strength.class))
                        : null;
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("Error converting JSON to Strength", e);
            }
        }
    }

    @Converter
    public static class SkillConverter implements AttributeConverter<Skill, String> {
        @Override
        public String convertToDatabaseColumn(Skill skill) {
            try {
                return objectMapper.writeValueAsString(skill);
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("Error converting Skill to JSON", e);
            }
        }

        @Override
        public Skill convertToEntityAttribute(String json) {
            try {
                return json != null ? objectMapper.readValue(json, Skill.class) : null;
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("Error converting JSON to Skill", e);
            }
        }
    }
}