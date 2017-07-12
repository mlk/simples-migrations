package com.github.mlk.simples.migrations;

import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/** Works out which of the beans known by Spring should be used to perform the migration.
 *
 * Implementers notes: Your configuration will need to return one of theses.
 */
@Component
@AllArgsConstructor
public class MigrationsFinder {
  private final ApplicationContext context;

  /** All the beans!
   *
   * @return Beans!
   */
  public Map<String, Migration> find() {
    return context.getBeansOfType(Migration.class);
  }
}
