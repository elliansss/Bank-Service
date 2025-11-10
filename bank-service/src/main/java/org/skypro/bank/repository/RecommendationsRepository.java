package org.skypro.bank.repository;

import org.skypro.bank.dto.RecommendationDTO;
import java.util.UUID;

public interface RecommendationsRepository {
    Integer countProductsByTypeAndUserId(UUID userId, String type);
    Integer getTransactionalSumByTypeAndUserIdAndOperationType(UUID userId, String type, String operationType);
    RecommendationDTO getProductById(UUID productId);
}