package org.skypro.bank.service;

import org.skypro.bank.dto.RecommendationDTO;
import org.skypro.bank.dto.RecommendationResponse;
import org.skypro.bank.entity.User;
import org.skypro.bank.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TelegramBotService {

    private final RecommendationService recommendationService;
    private final UserRepository userRepository;

    public TelegramBotService(RecommendationService recommendationService,
                              UserRepository userRepository) {
        this.recommendationService = recommendationService;
        this.userRepository = userRepository;
    }

    public String getWelcomeMessage() {
        return """
            🤖 *Добро пожаловать в банковского бота!*
            
            *Доступные команды:*
            /recommend [Имя Фамилия] - получить персональные рекомендации
            
            *Пример:* /recommend Иван Иванов
            
            💡 *Примечание:* Для работы бота нужен точный ввод имени и фамилии, как в базе данных банка.
            """;
    }

    public String getRecommendationsForUser(String username) {
        if (username == null || username.trim().isEmpty()) {
            return "❌ *Неверный формат.* Используйте: /recommend Имя Фамилия";
        }

        String[] nameParts = username.split(" ");

        if (nameParts.length != 2) {
            return "❌ *Неверный формат.* Используйте: /recommend Имя Фамилия\n\n*Пример:* /recommend Иван Иванов";
        }

        String firstName = nameParts[0].trim();
        String lastName = nameParts[1].trim();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            return "❌ *Неверный формат.* Имя и фамилия не могут быть пустыми";
        }

        Optional<User> user = userRepository.findUserByName(firstName, lastName);

        if (user.isEmpty()) {
            return "❌ *Пользователь не найден*\n\nПроверьте правильность ввода имени и фамилии.";
        }

        RecommendationResponse response = recommendationService.getRecommendations(user.get().getId());
        List<RecommendationDTO> recommendations = response.getRecommendations();

        if (recommendations.isEmpty()) {
            return String.format(
                    "👋 *Здравствуйте, %s %s!*\n\n" +
                            "На данный момент у нас нет персональных рекомендаций для вас.\\n\" +\n" +
                            "Продолжайте пользоваться нашими услугами, и мы обязательно предложим вам что-то интересное! 💫",
                    firstName, lastName
            );
        }

        StringBuilder message = new StringBuilder();
        message.append(String.format("👋 *Здравствуйте, %s %s!*\n\n", firstName, lastName));
        message.append("🎯 *Новые продукты для вас:*\n\n");

        for (int i = 0; i < recommendations.size(); i++) {
            RecommendationDTO rec = recommendations.get(i);
            message.append(String.format("*%d. %s*\n", i + 1, rec.getName()));
            message.append(String.format("   %s\n\n", rec.getText()));
        }

        message.append("💫 *Хотите узнать подробности? Обратитесь в наш офис!*");

        return message.toString();
    }
}