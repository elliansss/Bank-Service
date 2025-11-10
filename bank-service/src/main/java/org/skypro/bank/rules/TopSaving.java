package org.skypro.bank.rules;

import org.skypro.bank.dto.RecommendationDTO;
import org.skypro.bank.repository.RecommendationsRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class TopSaving implements RecommendationRuleSet {

    private static final UUID PRODUCT_ID = UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925");
    private static final String NAME = "Top Saving";
    private static final String DESCRIPTION =
            "Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели. Больше никаких забытых чеков и потерянных квитанций — всё под контролем!\n\n" +
                    "Преимущества «Копилки»:\n\n" +
                    "Накопление средств на конкретные цели. Установите лимит и срок накопления, и банк будет автоматически переводить определенную сумму на ваш счет.\n\n" +
                    "Прозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс накопления и корректируйте стратегию при необходимости.\n\n" +
                    "Безопасность и надежность. Ваши средства находятся под защитой банка, а доступ к ним возможен только через мобильное приложение или интернет-банкинг.\n\n" +
                    "Начните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!";

    private final RecommendationsRepository repository;

    public TopSaving(RecommendationsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDTO> apply(UUID userId) {
        Integer debitCount = repository.countProductsByTypeAndUserId(userId, "DEBIT");
        Integer debitDeposits = repository.getTransactionalSumByTypeAndUserIdAndOperationType(userId, "DEBIT", "DEPOSIT");
        Integer savingDeposits = repository.getTransactionalSumByTypeAndUserIdAndOperationType(userId, "SAVING", "DEPOSIT");
        Integer debitExpenses = repository.getTransactionalSumByTypeAndUserIdAndOperationType(userId, "DEBIT", "WITHDRAWAL");

        boolean usesDebit = debitCount > 0;
        boolean depositsOver50k = (debitDeposits >= 50000) || (savingDeposits >= 50000);
        boolean depositsGreaterThanExpenses = debitDeposits > debitExpenses;

        if (usesDebit && depositsOver50k && depositsGreaterThanExpenses) {
            return Optional.of(
                    new RecommendationDTO(
                            PRODUCT_ID,
                            NAME,
                            DESCRIPTION
                    )
            );
        }
        return Optional.empty();
    }
}