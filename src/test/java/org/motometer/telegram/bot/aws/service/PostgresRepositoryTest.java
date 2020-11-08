package org.motometer.telegram.bot.aws.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

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

  @Test
  void shouldFindStats() {
    final PostgresRepository postgresRepository = new PostgresRepository(
      "jdbc:postgresql://localhost:5432/cube",
      "cube_user",
      "cube_user"
    );

    final List<UserStats> statsByChatId = postgresRepository.findStatsByChatId(-1001211410985L);

    assertThat(statsByChatId).isNotNull();
  }
}
