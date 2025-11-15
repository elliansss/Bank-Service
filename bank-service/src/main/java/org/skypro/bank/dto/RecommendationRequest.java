package org.skypro.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Collection;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationRequest {
    private UUID product_id;           // Идентификатор продукта
    private String product_name;       // Имя продукта
    private String product_text;       // Описание продукта
    private Collection<RuleDto> rules;

    public UUID getProduct_id() {
        return product_id;
    }

    public void setProduct_id(UUID product_id) {
        this.product_id = product_id;
    }

    public Collection<RuleDto> getRules() {
        return rules;
    }

    public void setRules(Collection<RuleDto> rules) {
        this.rules = rules;
    }

    public String getProduct_text() {
        return product_text;
    }

    public void setProduct_text(String product_text) {
        this.product_text = product_text;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

}



