package org.motometer.bot.telegram.publisher;

import org.motometer.telegram.bot.Bot;
import org.motometer.telegram.bot.core.BotBuilder;

final class BotConfig {

    private BotConfig() {

    }

    static Bot bot() {
        return BotBuilder.defaultBuilder()
            .token(System.getenv("TELEGRAM_TOKEN"))
            .build();
    }
}
