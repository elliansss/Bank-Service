package org.skypro.bank.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;


import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "recommendation")
@Data
public class Recommendation {
    @Id
    private UUID id;

    private String name;
    private String text;

    @OneToMany(mappedBy = "recommendation")
    private List<Rule> rule;

    public Recommendation() {
    }

    public Recommendation(UUID id, List<Rule> rule, String text, String name) {
        this.id = id;
        this.rule = rule;
        this.text = text;
        this.name = name;
    }

    public List<Rule> getRule() {
        return rule;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setRule(List<Rule> rule) {
        this.rule = rule;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
