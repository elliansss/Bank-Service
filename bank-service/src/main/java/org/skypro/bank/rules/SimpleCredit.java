package org.skypro.bank.rules;

import org.skypro.bank.dto.RecommendationDTO;
import org.skypro.bank.repository.RecommendationsRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class SimpleCredit implements RecommendationRuleSet {

    private static final UUID PRODUCT_ID = UUID.fromString("ab138afb-f3ba-4a93-b74f-0fcee86d447f");
    private static final String NAME = "Простой кредит";
    private static final String DESCRIPTION =
            "Откройте мир выгодных кредитов с нами!\n\n" +
                    "Ищете способ быстро и без лишних хлопот получить нужную сумму? Тогда наш выгодный кредит — именно то, что вам нужно! Мы предлагаем низкие процентные ставки, гибкие условия и индивидуальный подход к каждому клиенту.\n\n" +
                    "Почему выбирают нас:\n\n" +
                    "Быстрое рассмотрение заявки. Мы ценим ваше время, поэтому процесс рассмотрения заявки занимает всего несколько часов.\n\n" +
                    "Удобное оформление. Подать заявку на кредит можно онлайн на нашем сайте или в мобильном приложении.\n\n" +
                    "Широкий выбор кредитных продуктов. Мы предлагаем кредиты на различные цели: покупку недвижимости, автомобиля, образование, лечение и многое другое.\n\n" +
                    "Не упустите возможность воспользоваться выгодными условиями кредитования от нашей компании!";

    private final RecommendationsRepository repository;

    public SimpleCredit(RecommendationsRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<RecommendationDTO> apply(UUID userId) {
        Integer creditCount = repository.countProductsByTypeAndUserId(userId, "CREDIT");
        Integer debitDeposits = repository.getTransactionalSumByTypeAndUserIdAndOperationType(userId, "DEBIT", "DEPOSIT");
        Integer debitExpenses = repository.getTransactionalSumByTypeAndUserIdAndOperationType(userId, "DEBIT", "WITHDRAWAL");

        boolean notUsesCredit = creditCount == 0;
        boolean depositsGreaterThanExpenses = debitDeposits > debitExpenses;
        boolean expensesOver100k = debitExpenses > 100000;

        if (notUsesCredit && depositsGreaterThanExpenses && expensesOver100k) {
            return List.of(
                    new RecommendationDTO(
                            PRODUCT_ID,
                            NAME,
                            DESCRIPTION
                    )
            );
        }
        return List.of();
    }
}