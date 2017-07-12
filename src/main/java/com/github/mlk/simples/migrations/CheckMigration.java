package com.github.mlk.simples.migrations;

/**
 * Checks which migrations have been performed.
 */
public interface CheckMigration {

  /** Performs any setup required (such as creating a migrations table).
   *
   * Note For Implementers: This should be done in such a way it can be executed multiple times
   */
  void setup();

  /** Has a migration been executed?
   *
   * @param migrationName The name of the migration
   * @return If the migration script has been executed.
   */
  boolean hasRunMigration(String migrationName);

  /** Sets if a migration has happened.
   *
   * @param migrationName The name of the migration.
   */
  void migrated(String migrationName);
}
