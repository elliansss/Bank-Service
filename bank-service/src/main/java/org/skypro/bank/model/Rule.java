package org.skypro.bank.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "rule")
public class Rule {
    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private QueryType queryType;
    private Boolean negate = false;
    @Convert(converter = ArgumentListConverter.class)
    private List<String> arguments=new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="recommendation_id")
    private Recommendation recommendation;

    public Rule() {
    }

    public Rule(UUID id, Recommendation recommendation, List<String> arguments, Boolean negate, QueryType queryType) {
        this.id = id;
        this.recommendation = recommendation;
        this.arguments = arguments;
        this.negate = negate;
        this.queryType = queryType;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Recommendation getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(Recommendation recommendation) {
        this.recommendation = recommendation;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public Boolean getNegate() {
        return negate;
    }

    public void setNegate(Boolean negate) {
        this.negate = negate;
    }

    public QueryType getQueryType() {
        return queryType;
    }

    public void setQueryType(QueryType queryType) {
        this.queryType = queryType;
    }
}
