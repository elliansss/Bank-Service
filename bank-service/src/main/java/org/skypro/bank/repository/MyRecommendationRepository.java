package org.skypro.bank.repository;

import org.skypro.bank.model.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MyRecommendationRepository extends JpaRepository<Recommendation, UUID> {
}
