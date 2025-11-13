package org.skypro.bank.rules;

import org.skypro.bank.dto.RecommendationDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {
   public List<RecommendationDTO> apply(UUID userId);
}
