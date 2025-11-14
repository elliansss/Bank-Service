package org.skypro.bank.service;

import org.skypro.bank.dto.RecommendationDTO;
import org.skypro.bank.dto.RecommendationRequest;
import org.skypro.bank.model.Recommendation;
import org.skypro.bank.repository.MyRecommendationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final MyRecommendationRepository myRecommendationRepository;

    public RecommendationServiceImpl(MyRecommendationRepository myRecommendationRepository) {
        this.myRecommendationRepository = myRecommendationRepository;
    }

    @Override
    public List<RecommendationDTO> getAllRecommendations() {
        return myRecommendationRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RecommendationDTO> getRecommendation(UUID id) {
        return myRecommendationRepository.findById(id)
                .map(this::convertToDto);
    }

    @Override
    public RecommendationDTO createRecommendation(RecommendationRequest request) {
        Recommendation recommendation = new Recommendation();
        recommendation.setId(UUID.randomUUID()); // генерируем новый UUID
        recommendation.setName(request.getName());
        recommendation.setText(request.getDescription());
        return convertToDto(myRecommendationRepository.save(recommendation));
    }

    @Override
    public void deleteRecommendation(UUID id) {
        myRecommendationRepository.deleteById(id);
    }

    private RecommendationDTO convertToDto(Recommendation entity) {
        return new RecommendationDTO(entity.getId(), entity.getName(), entity.getText());
    }
}