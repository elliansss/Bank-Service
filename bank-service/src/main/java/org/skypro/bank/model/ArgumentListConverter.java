package org.skypro.bank.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;
import java.util.stream.Collectors;

@Converter
public class ArgumentListConverter implements AttributeConverter<List<String>,String> {


    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        return attribute.stream().collect(Collectors.joining(","));
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        return List.of(dbData.split(","));
    }
}
