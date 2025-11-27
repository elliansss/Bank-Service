package org.skypro.bank.controller;

import org.skypro.bank.dto.RecommendationDTO;
import org.skypro.bank.dto.RecommendationRequest;
import org.skypro.bank.service.RecommendationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    // Получение списка всех рекомендаций
    @GetMapping("/")
    public ResponseEntity<List<RecommendationDTO>> getAllRecommendations() {
        return new ResponseEntity<>(recommendationService.getAllRecommendations(), HttpStatus.OK);
    }

    // Получение рекомендации по ID
    @GetMapping("/{id}")
    public ResponseEntity<Optional<RecommendationDTO>> getRecommendationById(@PathVariable UUID id) {
        return new ResponseEntity<>(recommendationService.getRecommendation(id), HttpStatus.OK);
    }

    // Создание новой рекомендации
    @PostMapping("/")
    public ResponseEntity<RecommendationDTO> createRecommendation(@RequestBody RecommendationRequest request) {
        return new ResponseEntity<>(recommendationService.createRecommendation(request), HttpStatus.CREATED);
    }

    // Удаление рекомендации по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecommendation(@PathVariable UUID id) {
        recommendationService.deleteRecommendation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}