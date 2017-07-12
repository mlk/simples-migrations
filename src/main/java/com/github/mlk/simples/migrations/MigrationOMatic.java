package com.github.mlk.simples.migrations;

import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/** Runs the migrations on application startup.
 *
 * Implementers notes: Your configuration will need to return one of theses.
 */
@Component
@AllArgsConstructor
public class MigrationOMatic implements ApplicationListener<ApplicationReadyEvent> {
  private final MigrationsFinder migrationsFinder;
  private final CheckMigration checkMigration;

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    checkMigration.setup();

    Map<String, Migration> migrations = migrationsFinder.find();

    migrations.entrySet()
        .stream()
        .filter(x -> !checkMigration.hasRunMigration(x.getKey()))
        .sorted((x, y) -> String.CASE_INSENSITIVE_ORDER.compare(x.getKey(), y.getKey()))
        .forEach(x -> {
          x.getValue().performMigration();
          checkMigration.migrated(x.getKey());
        });

  }
}
