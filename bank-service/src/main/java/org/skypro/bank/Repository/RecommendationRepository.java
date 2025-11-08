package org.skypro.bank.Repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RecommendationRepository {

    private final JdbcTemplate jdbcTemplate;

    public RecommendationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean hasProductType(String userId, String productType) {
        String sql = """
                SELECT COUNT(*) > 0
                FROM transactions t
                JOIN products p ON t.product_id = p.id
                WHERE t.user_id = ? AND p.type = ?
                """;

        return jdbcTemplate.queryForObject(sql, Boolean.class, userId, productType);
    }

    public double getTotalDepositsByProductType(String userId, String productType) {
        String sql = """
            SELECT COALESCE(SUM(t.amount), 0)
            FROM transactions t
            JOIN products p ON t.product_id = p.id
            WHERE t.user_id = ? AND p.type = ? AND t.operation_type = 'DEPOSIT'
            """;

        Double result = jdbcTemplate.queryForObject(sql, Double.class, userId, productType);
        return result != null ? result : 0.0;
    }

    public double getTotalExpensesByProductType(String userId, String productType) {
        String sql = """
            SELECT COALESCE(SUM(t.amount), 0)
            FROM transactions t
            JOIN products p ON t.product_id = p.id
            WHERE t.user_id = ? AND p.type = ? AND t.operation_type = 'EXPENSE'
            """;

        Double result = jdbcTemplate.queryForObject(sql, Double.class, userId, productType);
        return result != null ? result : 0.0;
    }
}