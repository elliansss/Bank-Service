package org.skypro.bank.rules;

import org.skypro.bank.dto.RecommendationDTO;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {
   public Optional<RecommendationDTO> apply(UUID userId);
}
