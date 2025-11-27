package org.skypro.bank.rules;

import org.skypro.bank.dto.RecommendationDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {
   Optional<RecommendationDTO> apply(UUID userId);

   /**
    * Возвращает идентификатор правила для статистики
    */
   default String getRuleId() {
      return this.getClass().getSimpleName();
   }
}
