package org.skypro.bank.service;

import org.skypro.bank.dto.RecommendationDTO;
import org.skypro.bank.dto.RecommendationRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecommendationService {
    List<RecommendationDTO> getAllRecommendations();              // Получить список всех рекомендаций
    Optional<RecommendationDTO> getRecommendation(UUID id);       // Найти рекомендацию по её ID
    RecommendationDTO createRecommendation(RecommendationRequest request); // Создать новую рекомендацию
    void deleteRecommendation(UUID id);                           // Удалить рекомендацию по её ID
}