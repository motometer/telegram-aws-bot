package org.motometer.telegram.bot.aws.service;

public interface WebHookListener {

  void onUpdate(final String update);
}
