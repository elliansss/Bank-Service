package org.skypro.bank.rules;

import org.skypro.bank.dto.RecommendationDTO;
import org.skypro.bank.model.Recommendation;
import org.skypro.bank.model.Rule;
import org.skypro.bank.repository.MyRecommendationRepository;
import org.skypro.bank.repository.RecommendationsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hibernate.internal.util.collections.ArrayHelper.forEach;
import static org.skypro.bank.model.QueryType.*;

@Service
public class DinamicRule implements RecommendationRuleSet {
    private final MyRecommendationRepository repository;
private final RecommendationsRepository recRepository;
    public DinamicRule(MyRecommendationRepository repository) {
        this.repository = repository;

    }

    @Override
    public List<RecommendationDTO> apply(UUID userID) {
        List<Recommendation> all = repository.findAll();


    all.stream()
            .filter(it  ->
                checkRules(it.getRule().userId))
            .map(recommendation -> new RecommendationDTO(
                    recommendation.getId(),
                    recommendation.getText(),
                    recommendation.getName

            )).toList();



    return Optional.empty();
}
private boolean checkRules(List <Rule> rule){
     return    rule.stream()
                .allMatch(it-> {
                    switch (it.getQueryType()) {
                        case USER_OF -> {
                            String type = it.getArguments().get(0);
                            boolean predicate = recRepository.countProductsByTypeAndUserId(userId.type) >= 1;

                            if (it.getNegate()) {
                                return !predicate;
                            } else {
                                return predicate;

                            }
                        }
                        case ACTIVE_USER_OF -> {
                            String type = it.getArguments().get(0);
                            boolean predicate = recRepository.countProductsByTypeAndUserId(userId.type) >= 5;

                            if (it.getNegate()) {
                                return !predicate;
                            } else {
                                return predicate;


                            }

                        }
                        case TRANSACTION_SUM_COMPARE -> {
                        }
                        case TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW -> {
                        }
                    }
                    return null;
                })
}
}