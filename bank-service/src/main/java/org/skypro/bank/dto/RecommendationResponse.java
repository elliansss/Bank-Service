package org.skypro.bank.dto;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class RecommendationResponse {
    private UUID userId;
    private List<RecommendationDTO> recommendations;

    public RecommendationResponse() {}

    public RecommendationResponse(UUID userId, List<RecommendationDTO> recommendations) {
        this.userId = userId;
        this.recommendations = recommendations;
    }
}