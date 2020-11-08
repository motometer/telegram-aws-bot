package org.motometer.telegram.bot.aws.service;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class PostgresRepository {

  static final String JDBC_DRIVER = "org.postgresql.Driver";

  private final String jdbcUrl;
  private final String userName;
  private final String password;
  private static final String INSERT_QUERY = "INSERT INTO UPDATES (created_at, raw_update) values (?, ?::jsonb)";

  private static final String FIND_STATS = "select distinct u.raw_update #>> '{message,from,id}'         as userId, "
    + "                u.raw_update #>> '{message,from,first_name}' as firstName, "
    + "                count(*) as messageCount, "
    + "                max(u.created_at) "
    + "from updates u "
    + "where u.created_at > ? "
    + "AND u.raw_update #>> '{message,from,id}' IS NOT NULL "
    + "AND (u.raw_update #>> '{message,chat,id}')::bigint = ? "
    + "group by u.raw_update #>> '{message,from,id}', "
    + "         u.raw_update #>> '{message,from,first_name}' "
    + "order by count(*) desc";

  private static final Clock CLOCK = Clock.systemUTC();

  public void saveUpdate(final String update) {
    loadDriver();

    try (
      Connection conn = DriverManager.getConnection(jdbcUrl, userName, password);
      PreparedStatement stmt = conn.prepareStatement(INSERT_QUERY);
    ) {

      stmt.setTimestamp(1, Timestamp.from(CLOCK.instant()));
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

  public List<UserStats> findStatsByChatId(final long id) {

    loadDriver();

    try (
      Connection conn = DriverManager.getConnection(jdbcUrl, userName, password);
      PreparedStatement stmt = conn.prepareStatement(FIND_STATS);
    ) {

      final Instant instant = CLOCK.instant().truncatedTo(ChronoUnit.DAYS);
      stmt.setTimestamp(1, Timestamp.from(instant));
      stmt.setLong(2, id);

      final ResultSet rs = stmt.executeQuery();

      final ArrayList<UserStats> result = new ArrayList<>();

      while (rs.next()) {
        result.add(new UserStats(rs.getString("firstName"), rs.getLong("messageCount")));
      }

      return result;
    } catch (SQLException se) {
      throw new RuntimeException(se);
    }
  }
}
