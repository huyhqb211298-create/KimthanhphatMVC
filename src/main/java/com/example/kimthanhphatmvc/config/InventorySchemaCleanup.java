package com.example.kimthanhphatmvc.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Removes the retired inventory module's tables. The statements are idempotent,
 * so the cleanup is safe on subsequent deployments after the first removal.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        name = "app.inventory.cleanup-enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class InventorySchemaCleanup implements ApplicationRunner {

    private static final String[] INVENTORY_TABLES = {
            "inventory_export_items",
            "inventory_import_items",
            "inventory_movements",
            "inventory_stocks",
            "inventory_exports",
            "inventory_imports"
    };

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) {
        for (String table : INVENTORY_TABLES) {
            jdbcTemplate.execute("DROP TABLE IF EXISTS " + table);
        }
        log.info("Retired inventory schema cleanup completed");
    }
}
