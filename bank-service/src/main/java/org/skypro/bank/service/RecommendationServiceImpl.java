package org.skypro.bank.service;

import org.skypro.bank.dto.RecommendationDTO;
import org.skypro.bank.dto.RecommendationRequest;
import org.skypro.bank.dto.RuleDto;
import org.skypro.bank.model.Recommendation;
import org.skypro.bank.model.Rule;
import org.skypro.bank.model.QueryType;
import org.skypro.bank.repository.MyRecommendationRepository;
import org.skypro.bank.repository.RulesRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final MyRecommendationRepository myRecommendationRepository;
    private final RulesRepository rulesRepository;

    public RecommendationServiceImpl(MyRecommendationRepository myRecommendationRepository, RulesRepository rulesRepository) {
        this.myRecommendationRepository = myRecommendationRepository;
        this.rulesRepository = rulesRepository;
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
     final  Recommendation recommendation = new Recommendation();
        recommendation.setId(UUID.randomUUID());                   // Генерируем новый UUID
        recommendation.setName(request.getProduct_name());        // Устанавливаем название продукта
        recommendation.setText(request.getProduct_text());        // Устанавливаем описание продукта

        // Преобразуем каждую переданную RuleDto в объект Rule
        Collection<Rule> rules = request.getRules().stream()
                .map(dto -> {
                    Rule rule = new Rule();
                    rule.setId(UUID.randomUUID());                 // Генерация нового UUID для каждого правила
                    rule.setQueryType(QueryType.valueOf(dto.getQueryType()));
                    rule.setArguments(dto.getArguments());
                    rule.setNegate(dto.isNegate());
                    rule.setRecommendation(recommendation);      // Связываем каждое правило с рекомендацией
                    return rule;
                })
                .collect(Collectors.toList());

        // Сохраняем сначала саму рекомендацию
        myRecommendationRepository.save(recommendation);

        // Затем сохраняем каждое правило отдельно

            rulesRepository.saveAll(rules);


        return convertToDto(recommendation);
    }

    @Override
    public void deleteRecommendation(UUID id) {
        myRecommendationRepository.deleteById(id);
    }

    private RecommendationDTO convertToDto(Recommendation entity) {
        return new RecommendationDTO(entity.getId(), entity.getName(), entity.getText());
    }
}