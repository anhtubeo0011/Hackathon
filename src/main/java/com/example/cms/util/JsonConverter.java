package com.example.cms.util;

import com.example.cms.entity.Strength;
import com.example.cms.entity.Weakness;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

public class JsonConverter {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Converter
  public static class StrengthConverter implements AttributeConverter<Strength, String> {
    @Override
    public String convertToDatabaseColumn(Strength strength) {
      try {
        return objectMapper.writeValueAsString(strength);
      } catch (JsonProcessingException e) {
        throw new IllegalArgumentException("Error converting Strength to JSON", e);
      }
    }

    @Override
    public Strength convertToEntityAttribute(String json) {
      try {
        return json != null ? objectMapper.readValue(json, Strength.class) : null;
      } catch (JsonProcessingException e) {
        throw new IllegalArgumentException("Error converting JSON to Strength", e);
      }
    }
  }

  @Converter
  public static class WeaknessConverter implements AttributeConverter<Weakness, String> {
    @Override
    public String convertToDatabaseColumn(Weakness weakness) {
      try {
        return objectMapper.writeValueAsString(weakness);
      } catch (JsonProcessingException e) {
        throw new IllegalArgumentException("Error converting Weakness to JSON", e);
      }
    }

    @Override
    public Weakness convertToEntityAttribute(String json) {
      try {
        return json != null ? objectMapper.readValue(json, Weakness.class) : null;
      } catch (JsonProcessingException e) {
        throw new IllegalArgumentException("Error converting JSON to Weakness", e);
      }
    }
  }
}