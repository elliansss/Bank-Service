package org.skypro.bank.model;


import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.UUID;

@Entity
@Table
@Data




public class Recommendation {
    public List<Rule> getRule;
    @Id UUID id;

    private String name;
    private String text;

    @OneToMany
    private List<Rule> rule;


}
