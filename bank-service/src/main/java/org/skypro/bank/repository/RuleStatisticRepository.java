package org.skypro.bank.repository;

import org.skypro.bank.entity.RuleStatistic;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RuleStatisticRepository {

    private final JdbcTemplate jdbcTemplate;

    public RuleStatisticRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void incrementCounter(String ruleId) {
        String sql = """
            INSERT INTO rule_statistics (rule_id, count) 
            VALUES (?, 1)
            ON CONFLICT (rule_id) 
            DO UPDATE SET count = rule_statistics.count + 1
            """;

        jdbcTemplate.update(sql, ruleId);
    }

    public List<RuleStatistic> getAllStatistics() {
        String sql = "SELECT rule_id, count FROM rule_statistics";

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new RuleStatistic(
                        rs.getString("rule_id"),
                        rs.getLong("count")
                ));
    }

    public void deleteStatistic(String ruleId) {
        String sql = "DELETE FROM rule_statistics WHERE rule_id = ?";
        jdbcTemplate.update(sql, ruleId);
    }
}