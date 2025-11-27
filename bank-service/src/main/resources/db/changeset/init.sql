CREATE TABLE recommendation
(
    id           UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    text TEXT         NOT NULL
);

CREATE TABLE rule
(
    id                UUID PRIMARY KEY,
    recommendation_id UUID         NOT NULL,
    query_type             VARCHAR(255) NOT NULL,
    arguments         TEXT      NOT NULL,
    negate            BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_rules_recommendation_id
        FOREIGN KEY (recommendation_id)
            REFERENCES recommendation (id)
            ON DELETE CASCADE
);

