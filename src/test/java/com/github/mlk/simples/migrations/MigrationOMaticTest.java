package com.github.mlk.simples.migrations;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.mockito.InOrder;

public class MigrationOMaticTest {

  @Test
  public void alwaysExecuteMigrationStartup() {
    CheckMigration checkMigration = mock(CheckMigration.class);
    MigrationsFinder migrationsFinder = mock(MigrationsFinder.class);

    when(migrationsFinder.find()).thenReturn(Collections.emptyMap());

    MigrationOMatic migrationOMatic = new MigrationOMatic(migrationsFinder, checkMigration);

    migrationOMatic.onApplicationEvent(null);

    verify(checkMigration).setup();
  }

  @Test
  public void whenMigrationHasBeenExecutedThenIgnoreMigration() {
    CheckMigration checkMigration = mock(CheckMigration.class);
    MigrationsFinder migrationsFinder = mock(MigrationsFinder.class);
    Migration migration = mock(Migration.class);

    when(migrationsFinder.find()).thenReturn(Collections.singletonMap("V1", migration));
    when(checkMigration.hasRunMigration("V1")).thenReturn(true);

    MigrationOMatic migrationOMatic = new MigrationOMatic(migrationsFinder, checkMigration);

    migrationOMatic.onApplicationEvent(null);

    verifyZeroInteractions(migration);
  }

  @Test
  public void whenMigrationHasNotBeenExecutedThenPerformMigration() {
    CheckMigration checkMigration = mock(CheckMigration.class);
    MigrationsFinder migrationsFinder = mock(MigrationsFinder.class);
    Migration migration = mock(Migration.class);

    when(migrationsFinder.find()).thenReturn(Collections.singletonMap("V1", migration));
    when(checkMigration.hasRunMigration("V1")).thenReturn(false);

    MigrationOMatic migrationOMatic = new MigrationOMatic(migrationsFinder, checkMigration);

    migrationOMatic.onApplicationEvent(null);

    verify(migration).performMigration();
    verify(checkMigration).migrated("V1");
  }

  @Test
  public void shoudlRunMigrationsInOrder() {
    CheckMigration checkMigration = mock(CheckMigration.class);
    MigrationsFinder migrationsFinder = mock(MigrationsFinder.class);
    Migration migration1 = mock(Migration.class);
    Migration migration2 = mock(Migration.class);

    Map<String, Migration> map = new HashMap<>();
    map.put("V2", migration2);
    map.put("V1", migration1);

    when(migrationsFinder.find()).thenReturn(map);
    when(checkMigration.hasRunMigration("V1")).thenReturn(false);
    when(checkMigration.hasRunMigration("V2")).thenReturn(false);

    MigrationOMatic migrationOMatic = new MigrationOMatic(migrationsFinder, checkMigration);

    migrationOMatic.onApplicationEvent(null);

    InOrder inOrder = inOrder(migration1, migration2);

    inOrder.verify(migration1).performMigration();
    inOrder.verify(migration2).performMigration();
  }
}