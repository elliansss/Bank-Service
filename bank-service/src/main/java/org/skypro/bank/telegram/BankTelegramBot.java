package org.skypro.bank.telegram;

import org.skypro.bank.service.TelegramBotService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class BankTelegramBot extends TelegramLongPollingBot {

    private final TelegramBotService telegramBotService;

    @Value("${telegram.bot.username:}")
    private String botUsername;

    public BankTelegramBot(@Value("${telegram.bot.token:}") String botToken,
                           TelegramBotService telegramBotService) {
        super(botToken);
        this.telegramBotService = telegramBotService;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String chatId = message.getChatId().toString();
            String text = message.getText();

            processMessage(chatId, text);
        }
    }

    private void processMessage(String chatId, String text) {
        try {
            String response;

            if (text.equals("/start")) {
                response = telegramBotService.getWelcomeMessage();
            } else if (text.startsWith("/recommend")) {
                String username = extractUsername(text);
                response = telegramBotService.getRecommendationsForUser(username);
            } else {
                response = "❌ Неизвестная команда. Используйте /start для справки.";
            }

            sendMessage(chatId, response);
        } catch (Exception e) {
            sendMessage(chatId, "❌ Произошла ошибка при обработке запроса. Попробуйте позже.");
        }
    }

    private String extractUsername(String text) {
        // Убираем команду и лишние пробелы
        return text.replace("/recommend", "").trim();
    }

    private void sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.enableMarkdown(true);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to send Telegram message", e);
        }
    }
}