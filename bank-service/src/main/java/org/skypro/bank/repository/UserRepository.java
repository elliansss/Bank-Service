package org.skypro.bank.repository;

import org.skypro.bank.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<User> findUserByName(String firstName, String lastName) {
        String sql = "SELECT id, first_name, last_name FROM USERS WHERE first_name = ? AND last_name = ?";

        try {
            User user = jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                    new User(
                            UUID.fromString(rs.getString("id")),
                            rs.getString("first_name"),
                            rs.getString("last_name")
                    ), firstName, lastName);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<User> findUsersByFirstName(String firstName) {
        String sql = "SELECT id, first_name, last_name FROM USERS WHERE first_name = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new User(
                        UUID.fromString(rs.getString("id")),
                        rs.getString("first_name"),
                        rs.getString("last_name")
                ), firstName);
    }
}