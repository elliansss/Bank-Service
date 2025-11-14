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

    public DinamicRule(MyRecommendationRepository repository, RecommendationsRepository recRepository) {
        this.repository = repository;

        this.recRepository = recRepository;
    }

    @Override
    public List<RecommendationDTO> apply(UUID userID) {

        return repository.findAll().stream()
                .filter(it -> checkRules(it.getRule(), userID))
                .map(recommendation -> new RecommendationDTO(
                        recommendation.getId(),
                        recommendation.getText(),
                        recommendation.getName()

                )).toList();
    }

    private boolean checkRules(List<Rule> rule, UUID userId) {
        return rule.stream()
                .allMatch(it -> {
                    switch (it.getQueryType()) {
                        case USER_OF -> {
                            String type = it.getArguments().get(0);
                            boolean predicate = recRepository.countProductsByTypeAndUserId(userId, type) >= 1;

                            if (it.getNegate()) {
                                return !predicate;
                            } else {
                                return predicate;

                            }
                        }
                        case ACTIVE_USER_OF -> {
                            String type = it.getArguments().get(0);
                            boolean predicate = recRepository.countProductsByTypeAndUserId(userId, type) >= 5;

                            if (it.getNegate()) {
                                return !predicate;
                            } else {
                                return predicate;


                            }

                        }
                        case TRANSACTION_SUM_COMPARE -> {
                            String type = it.getArguments().get(0);
                            String operationType = it.getArguments().get(1);
                            String sign = it.getArguments().get(0);
                            String sum = it.getArguments().get(1);
                            Integer result = recRepository.getTransactionalSumByTypeAndUserIdAndOperationType(userId, type, operationType);

                            boolean predicate = resolveBySign(sign, result,Integer.parseInt(sum));
                            if (it.getNegate()) {
                                return !predicate;
                            } else {
                                return predicate;

                            }

                        }
                        case TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW -> {
                            String type = it.getArguments().get(0);
                            String sign = it.getArguments().get(1);
                            Integer deposit = recRepository.getTransactionalSumByTypeAndUserIdAndOperationType(userId, type, "DEPOSIT");
                            Integer withdraw = recRepository.getTransactionalSumByTypeAndUserIdAndOperationType(userId, type, "WITHDRAW");

                            boolean predicate = resolveBySign(sign, deposit, withdraw);
                            if (it.getNegate()) {
                                return !predicate;
                            } else {
                                return predicate;


                            }
                        }
                    }

                    return false;

                });


    }

    private boolean resolveBySign(String sign, Integer fest, Integer second) {
        return switch (sign) {
            case ">" -> fest > second;
            case "<" -> fest < second;
            case ">=" -> fest >= second;
            case "<=" -> fest <= second;
            default ->  throw new IllegalArgumentException();
        };
    }
}