package org.skypro.bank.controller;

import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/management")
public class ManagementController {

    private final BuildProperties buildProperties;
    private final JdbcTemplate jdbcTemplate;

    public ManagementController(BuildProperties buildProperties, JdbcTemplate jdbcTemplate) {
        this.buildProperties = buildProperties;
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/clear-caches")
    public ResponseEntity<String> clearCaches() {
        return ResponseEntity.ok("Caches cleared successfully");
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> getServiceInfo() {
        return ResponseEntity.ok(Map.of(
                "name", buildProperties.getName(),
                "version", buildProperties.getVersion()
        ));
    }
}