package org.motometer.telegram.bot.aws.service;

import org.junit.jupiter.api.Test;

class PostgresRepositoryTest {

  @Test
  void shouldSaveUpdate() {
    final PostgresRepository postgresRepository = new PostgresRepository(
      "jdbc:postgresql://localhost:5432/cube",
      "cube_user",
      "cube_user"
    );

    postgresRepository.saveUpdate("{\"hello\": \"world\"}");
  }
}
