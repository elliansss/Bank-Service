package org.skypro.bank.impl;

import org.skypro.bank.dto.RecommendationDTO;
import org.skypro.bank.repository.RecommendationsRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class RecommendationsRepositoryJDBC implements RecommendationsRepository {

    private final JdbcTemplate jdbcTemplate;

    public RecommendationsRepositoryJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer countProductsByTypeAndUserId(UUID userId, String type) {
        String sql = """
            SELECT COUNT(DISTINCT p.id) 
            FROM TRANSACTION t 
            JOIN PRODUCT p ON t.product_id = p.id 
            WHERE t.user_id = ? AND p.type = ?
            """;

        Integer result = jdbcTemplate.queryForObject(sql, Integer.class, userId, type);
        return result != null ? result : 0;
    }

    @Override
    public Integer getTransactionalSumByTypeAndUserIdAndOperationType(UUID userId, String type, String operationType) {
        String sql = """
            SELECT COALESCE(SUM(t.amount), 0) 
            FROM TRANSACTION t 
            JOIN PRODUCT p ON t.product_id = p.id 
            WHERE t.user_id = ? AND p.type = ? AND t.operation_type = ?
            """;

        Integer result = jdbcTemplate.queryForObject(sql, Integer.class, userId, type, operationType);
        return result != null ? result : 0;
    }

    @Override
    public RecommendationDTO getProductById(UUID productId) {
        String sql = "SELECT id, name, description FROM PRODUCT WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new RecommendationDTO(
                        UUID.fromString(rs.getString("id")),
                        rs.getString("name"),
                        rs.getString("description")
                ), productId);
    }
}