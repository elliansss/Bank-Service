package org.skypro.bank.controller;

import org.skypro.bank.dto.RecommendationResponse;
import org.skypro.bank.service.RecommendationService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{userId}")
    public RecommendationResponse getRecommendations(@PathVariable UUID userId) {
        return recommendationService.getRecommendations(userId);
    }
}