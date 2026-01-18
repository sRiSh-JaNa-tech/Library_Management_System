package com.LibManSys.LibManSys.Repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean userExists(String username, String password) {

        String sql = "SELECT COUNT(*) FROM users WHERE name = ? AND password = ?";

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username, password);

        return count != null && count > 0;
    }
}
