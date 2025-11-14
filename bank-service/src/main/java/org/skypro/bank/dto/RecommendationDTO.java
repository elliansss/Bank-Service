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

    public void setName(String name) {
        this.name = name;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }
}