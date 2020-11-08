package org.motometer.telegram.bot.aws.service;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
public class UpdateListener implements WebHookListener {

  private final PostgresRepository postgresRepository;

  @Override
  public void onUpdate(final String update) {
    postgresRepository.saveUpdate(update);
  }
}
