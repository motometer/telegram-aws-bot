package org.motometer.telegram.bot.aws;

import org.junit.jupiter.api.Test;
import org.motometer.telegram.bot.api.ChatType;
import org.motometer.telegram.bot.api.ImmutableChat;
import org.motometer.telegram.bot.api.ImmutableMessage;
import org.motometer.telegram.bot.api.SendMessage;

import static org.assertj.core.api.Assertions.assertThat;

class SendMessageFactoryTest {

    @Test
    void createMessage() {
        SendMessageFactory factory = new SendMessageFactory();
        int chatId = 321;
        ImmutableMessage input = ImmutableMessage.builder()
            .id(1)
            .date(0)
            .chat(ImmutableChat.builder().id(chatId).type(ChatType.PRIVATE_CHAT).build())
            .build();

        SendMessage message = factory.createMessage(input);

        assertThat(message).isNotNull();
        assertThat(message.chatId()).isEqualTo(chatId);
        assertThat(message.replyToMessageId()).isEqualTo(1);
        assertThat(message.text()).isEqualTo("Your message accepted.");
    }
}