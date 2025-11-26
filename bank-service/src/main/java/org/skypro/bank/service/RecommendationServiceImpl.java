package org.skypro.bank.service;

import org.skypro.bank.dto.RecommendationDTO;
import org.skypro.bank.dto.RecommendationResponse;
import org.skypro.bank.rules.RecommendationRuleSet;
import org.skypro.bank.repository.RuleStatisticRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final List<RecommendationRuleSet> ruleSets;
    private final RuleStatisticRepository ruleStatisticRepository;

    public RecommendationServiceImpl(List<RecommendationRuleSet> ruleSets,
                                     RuleStatisticRepository ruleStatisticRepository) {
        this.ruleSets = ruleSets;
        this.ruleStatisticRepository = ruleStatisticRepository;
    }

    @Override
    public RecommendationResponse getRecommendations(UUID userId) {
        List<RecommendationDTO> recommendations = ruleSets.stream()
                .map(rule -> {
                    Optional<RecommendationDTO> recommendation = rule.apply(userId);
                    if (recommendation.isPresent()) {
                        String ruleId = getRuleId(rule);
                        if (ruleId != null) {
                            ruleStatisticRepository.incrementCounter(ruleId);
                        }
                    }
                    return recommendation;
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return new RecommendationResponse(userId, recommendations);
    }

    private String getRuleId(RecommendationRuleSet rule) {
        return rule.getClass().getSimpleName();
    }
}