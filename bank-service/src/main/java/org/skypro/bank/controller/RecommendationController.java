package org.skypro.bank.controller;

import org.skypro.bank.model.Recommendation;
import org.skypro.bank.model.RecommendationResponse;
import org.skypro.bank.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<RecommendationResponse> getRecommendations(
            @PathVariable String userId) {

        try {
            UUID.fromString(userId);

            List<Recommendation> recommendations = recommendationService.getRecommendationsForUser(userId);

            RecommendationResponse response = new RecommendationResponse(userId, recommendations);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            RecommendationResponse response = new RecommendationResponse(userId, List.of());
            return ResponseEntity.ok(response);
        }
    }
}