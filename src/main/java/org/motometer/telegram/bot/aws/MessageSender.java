package org.motometer.telegram.bot.aws;

import lombok.RequiredArgsConstructor;
import org.motometer.telegram.bot.Bot;
import org.motometer.telegram.bot.UpdateListener;
import org.motometer.telegram.bot.api.SendMessage;
import org.motometer.telegram.bot.api.Update;

@RequiredArgsConstructor
public class MessageSender implements UpdateListener {

    private final Bot bot;
    private final SendMessageFactory sendMessageFactory = new SendMessageFactory();

    @Override
    public void onEvent(Update event) {
        SendMessage message = sendMessageFactory.createMessage(event.message());
        bot.sendMessage(message);
    }
}
