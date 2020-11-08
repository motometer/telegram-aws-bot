package org.motometer.telegram.bot.aws.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.motometer.telegram.bot.Bot;
import org.motometer.telegram.bot.api.Chat;
import org.motometer.telegram.bot.api.ImmutableSendMessage;
import org.motometer.telegram.bot.api.Update;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommandListener implements WebHookListener {

  private final Bot bot;
  private final Gson gson;
  private final PostgresRepository postgresRepository;
  private final LambdaLogger log;

  @Override
  public void onUpdate(final String update) {
    final Update parsedUpdate = gson.fromJson(update, Update.class);

    Optional.ofNullable(parsedUpdate.message())
      .ifPresent(message -> {
        final String text = message.text();

        if ("/stats".equals(text) || "/stats@motometer_test_bot".equals(text)) {
          final Chat chat = message.chat();

          final List<UserStats> statsByChatId = postgresRepository.findStatsByChatId(chat.id());

          final String stats = statsByChatId.stream()
            .map(v -> String.join(",", v.getUserName(), String.valueOf(v.getMessageCount())))
            .collect(Collectors.joining("\n"));

          log.log("Sending a message: " + stats);
          final ImmutableSendMessage sendMessage = ImmutableSendMessage.of(chat.id(), stats);

          bot.sendMessage(sendMessage);

          log.log("Message has been sent");
        }
      });
  }
}
