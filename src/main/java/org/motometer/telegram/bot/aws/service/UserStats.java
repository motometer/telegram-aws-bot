package org.motometer.telegram.bot.aws.service;

import lombok.Data;

@Data
public class UserStats {

  private final String userName;
  private final long messageCount;
}
