package org.skypro.bank.rules;

import org.skypro.bank.dto.RecommendationDTO;
import org.skypro.bank.repository.RecommendationsRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class Invest500 implements RecommendationRuleSet {

    private static final UUID PRODUCT_ID = UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a");
    private static final String NAME = "Invest 500";
    private static final String DESCRIPTION =
            "Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! " +
                    "Воспользуйтесь налоговыми льготами и начните инвестировать с умом. " +
                    "Пополните счет до конца года и получите выгоду в виде вычета на взнос " +
                    "в следующем налоговом периоде. " +
                    "Не упустите возможность разнообразить свой портфель, снизить риски " +
                    "и следить за актуальными рыночными тенденциями. " +
                    "Откройте ИИС сегодня и станьте ближе к финансовой независимости!";

    private final RecommendationsRepository repository;

    public Invest500(RecommendationsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDTO> apply(UUID userId) {
        Integer debitCount = repository.countProductsByTypeAndUserId(userId, "DEBIT");
        Integer investCount = repository.countProductsByTypeAndUserId(userId, "INVEST");
        Integer sum = repository.getTransactionalSumByTypeAndUserIdAndOperationType(userId, "SAVING", "DEPOSIT");

        if (debitCount > 0 && investCount == 0 && sum > 1000) {
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