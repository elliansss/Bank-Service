package org.skypro.bank.controller;

import org.skypro.bank.service.TelegramBotService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/telegram")
public class TelegramWebController {

    private final TelegramBotService telegramBotService;

    public TelegramWebController(TelegramBotService telegramBotService) {
        this.telegramBotService = telegramBotService;
    }

    @GetMapping("/welcome")
    public String getWelcomeMessage() {
        return telegramBotService.getWelcomeMessage();
    }

    @PostMapping("/recommend")
    public String getRecommendations(@RequestParam String username) {
        return telegramBotService.getRecommendationsForUser(username);
    }
}