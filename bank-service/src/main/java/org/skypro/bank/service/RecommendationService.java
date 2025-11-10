package org.skypro.bank.service;

import org.skypro.bank.dto.RecommendationResponse;

import java.util.UUID;

public interface RecommendationService {
    RecommendationResponse getRecommendations(UUID userId);
}