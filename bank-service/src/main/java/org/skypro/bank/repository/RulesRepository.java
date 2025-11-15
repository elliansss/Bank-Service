package org.skypro.bank.repository;

import org.skypro.bank.model.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface RulesRepository extends JpaRepository<Rule, UUID> { }