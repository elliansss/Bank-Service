package org.skypro.bank.impl;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.skypro.bank.dto.RecommendationDTO;
import org.skypro.bank.repository.RecommendationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Repository
public class RecommendationsRepositoryJDBC implements RecommendationsRepository {

    private final JdbcTemplate jdbcTemplate;

    // Конфигурация кеша
    private final LoadingCache<Object[], Object> cacheCountProductsByTypeAndUserId;
    private final LoadingCache<Object[], Object> cacheGetTransactionalSumByTypeAndUserIdAndOperationType;
    private final LoadingCache<UUID, RecommendationDTO> cacheGetProductById;

    @Autowired
    public RecommendationsRepositoryJDBC(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        // Настройки кеша: максимальный размер и длительность хранения записи
        int maxSize = 10_000; // Максимальное количество элементов в каждом кэше
        Duration expireAfterAccessDuration = Duration.ofMinutes(10); // Время жизни каждой записи

        // Кэш для количества товаров определенного типа и пользователя
        cacheCountProductsByTypeAndUserId = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterAccess(expireAfterAccessDuration)
                .build(key -> countProductsByTypeAndUserIdInternal((UUID) key[0], (String) key[1]));

        // Кэш для суммы транзакций конкретного типа и пользователя
        cacheGetTransactionalSumByTypeAndUserIdAndOperationType = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterAccess(expireAfterAccessDuration)
                .build(key -> getTransactionalSumByTypeAndUserIdAndOperationTypeInternal((UUID) key[0], (String) key[1], (String) key[2]));

        // Кэш для конкретных продуктов по ID
        cacheGetProductById = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterAccess(expireAfterAccessDuration)
                .build(this::getProductByIdInternal);
    }

    @Override
    public Integer countProductsByTypeAndUserId(UUID userId, String type) {
        return (Integer) cacheCountProductsByTypeAndUserId.get(new Object[]{userId, type});
    }

    private Integer countProductsByTypeAndUserIdInternal(UUID userId, String type) {
        String sql = """
            SELECT COUNT(DISTINCT p.id) 
            FROM TRANSACTION t 
            JOIN PRODUCT p ON t.product_id = p.id 
            WHERE t.user_id = ? AND p.type = ?
            """;
        return jdbcTemplate.queryForObject(sql, Integer.class, userId, type);
    }

    @Override
    public Integer getTransactionalSumByTypeAndUserIdAndOperationType(UUID userId, String type, String operationType) {
        return (Integer) cacheGetTransactionalSumByTypeAndUserIdAndOperationType.get(new Object[] {userId, type, operationType});
    }

    private Integer getTransactionalSumByTypeAndUserIdAndOperationTypeInternal(UUID userId, String type, String operationType) {
        String sql = """
            SELECT COALESCE(SUM(t.amount), 0) 
            FROM TRANSACTION t 
            JOIN PRODUCT p ON t.product_id = p.id 
            WHERE t.user_id = ? AND p.type = ? AND t.operation_type = ?
            """;
        return jdbcTemplate.queryForObject(sql, Integer.class, userId, type, operationType);
    }

    @Override
    public RecommendationDTO getProductById(UUID productId) {
        return cacheGetProductById.get(productId);
    }

    private RecommendationDTO getProductByIdInternal(UUID productId) {
        String sql = "SELECT id, name, description FROM PRODUCT WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                        new RecommendationDTO(
                                UUID.fromString(rs.getString("id")),
                                rs.getString("name"),
                                rs.getString("description"))
                , productId);
    }
}