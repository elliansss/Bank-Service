package org.skypro.bank.service;

import org.skypro.bank.dto.RuleStatsResponse;
import org.skypro.bank.entity.RuleStatistic;
import org.skypro.bank.repository.RuleStatisticRepository;
import org.skypro.bank.rules.RecommendationRuleSet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RuleStatisticService {

    private final RuleStatisticRepository ruleStatisticRepository;
    private final List<RecommendationRuleSet> ruleSets;

    public RuleStatisticService(RuleStatisticRepository ruleStatisticRepository,
                                List<RecommendationRuleSet> ruleSets) {
        this.ruleStatisticRepository = ruleStatisticRepository;
        this.ruleSets = ruleSets;
    }

    public RuleStatsResponse getRuleStatistics() {
        List<RuleStatistic> dbStatistics = ruleStatisticRepository.getAllStatistics();
        Map<String, Long> statsMap = dbStatistics.stream()
                .collect(Collectors.toMap(RuleStatistic::getRuleId, RuleStatistic::getCount));

        List<RuleStatsResponse.RuleStatDTO> statsList = new ArrayList<>();

        for (RecommendationRuleSet rule : ruleSets) {
            String ruleId = getRuleId(rule);
            Long count = statsMap.getOrDefault(ruleId, 0L);
            statsList.add(new RuleStatsResponse.RuleStatDTO(ruleId, count));
        }

        return new RuleStatsResponse(statsList);
    }

    public void deleteRuleStatistics(String ruleId) {
        ruleStatisticRepository.deleteStatistic(ruleId);
    }

    private String getRuleId(RecommendationRuleSet rule) {
        return rule.getClass().getSimpleName();
    }
}