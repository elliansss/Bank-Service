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

    @ManyToMany
    @JoinColumn(name="recommendation")
    private Recommendation recommendation;
}
