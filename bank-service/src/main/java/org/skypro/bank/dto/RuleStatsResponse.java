package org.skypro.bank.dto;

import lombok.Data;
import java.util.List;

@Data
public class RuleStatsResponse {
    private List<RuleStatDTO> stats;

    public RuleStatsResponse() {}

    public RuleStatsResponse(List<RuleStatDTO> stats) {
        this.stats = stats;
    }

    @Data
    public static class RuleStatDTO {
        private String ruleId;
        private Long count;

        public RuleStatDTO() {}

        public RuleStatDTO(String ruleId, Long count) {
            this.ruleId = ruleId;
            this.count = count;
        }
    }
}