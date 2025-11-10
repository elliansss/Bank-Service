package org.skypro.bank.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class RecommendationDTO {
    private UUID id;
    private String name;
    private String text;

    public RecommendationDTO() {}

    public RecommendationDTO(UUID id, String name, String text) {
        this.id = id;
        this.name = name;
        this.text = text;
    }
}