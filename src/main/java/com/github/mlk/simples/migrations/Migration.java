package com.github.mlk.simples.migrations;

/** Migrations implement this and then are called (in Bean name order).
 *
 */
public interface Migration {

  /** The code to be executed when performing the migration. */
  void performMigration();
}
