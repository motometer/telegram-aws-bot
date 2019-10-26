package org.motometer.telegram.bot.aws;

import org.motometer.telegram.bot.Bot;
import org.motometer.telegram.bot.WebHookListener;
import org.motometer.telegram.bot.client.BotBuilder;

final class BotConfig {

    private BotConfig() {

    }

    static WebHookListener webHookListener() {
        Bot bot = bot();
        return bot.adaptListener(new MessageSender(bot));
    }

    private static Bot bot() {
        return BotBuilder.defaultBuilder()
            .token(System.getenv("TELEGRAM_TOKEN"))
            .build();
    }
}
