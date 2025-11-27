package org.skypro.bank.entity;

import lombok.Data;
import java.util.UUID;

@Data
public class RuleStatistic {
    private String ruleId;
    private Long count;

    public RuleStatistic() {}

    public RuleStatistic(String ruleId, Long count) {
        this.ruleId = ruleId;
        this.count = count;
    }

    // Явно добавляем геттеры для избежания проблем
    public String getRuleId() {
        return ruleId;
    }

    public Long getCount() {
        return count;
    }
}