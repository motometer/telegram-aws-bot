package org.motometer.telegram.bot.aws.service;


import com.amazonaws.services.lambda.runtime.LambdaLogger;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
public class UpdateListener implements WebHookListener {

  private final LambdaLogger log;
  private final PostgresRepository postgresRepository;

  @Override
  public void onUpdate(final String update) {
    log.log("Received update: " + update);
    postgresRepository.saveUpdate(update);
  }
}
