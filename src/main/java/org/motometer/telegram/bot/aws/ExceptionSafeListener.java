package org.motometer.telegram.bot.aws;

import org.motometer.telegram.bot.aws.service.WebHookListener;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExceptionSafeListener implements WebHookListener {

  private final WebHookListener listener;
  private final LambdaLogger log;

  @Override
  public void onUpdate(final String update) {
    try {
      listener.onUpdate(update);
    } catch (RuntimeException ex) {
      log.log("ERROR: " + ex.getMessage());
    }
  }
}
