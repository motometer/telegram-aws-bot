package org.motometer.telegram.bot.aws.service;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Clock;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class PostgresRepository {

  static final String JDBC_DRIVER = "org.postgresql.Driver";

  private final String jdbcUrl;
  private final String userName;
  private final String password;
  private static final String INSERT_QUERY = "INSERT INTO UPDATES (created_at, raw_update) values (?, ?::jsonb)";

  public void saveUpdate(final String update) {
    final Clock clock = Clock.systemUTC();

    loadDriver();

    try (
      Connection conn = DriverManager.getConnection(jdbcUrl, userName, password);
      PreparedStatement stmt = conn.prepareStatement(INSERT_QUERY);
    ) {

      stmt.setTimestamp(1, Timestamp.from(clock.instant()));
      stmt.setString(2, update);

      stmt.execute();

    } catch (SQLException se) {
      throw new RuntimeException(se);
    }
  }

  @SneakyThrows
  private void loadDriver() {
    Class.forName(JDBC_DRIVER);
  }

}
