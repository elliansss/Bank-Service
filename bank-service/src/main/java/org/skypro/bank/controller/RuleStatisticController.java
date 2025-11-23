package org.skypro.bank.controller;

import org.skypro.bank.dto.RuleStatsResponse;
import org.skypro.bank.service.RuleStatisticService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rule")
public class RuleStatisticController {

    private final RuleStatisticService ruleStatisticService;

    public RuleStatisticController(RuleStatisticService ruleStatisticService) {
        this.ruleStatisticService = ruleStatisticService;
    }

    @GetMapping("/stats")
    public RuleStatsResponse getRuleStats() {
        return ruleStatisticService.getRuleStatistics();
    }
}