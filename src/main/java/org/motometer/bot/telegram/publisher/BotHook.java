package org.motometer.bot.telegram.publisher;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.motometer.telegram.bot.Bot;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

public class BotHook implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final Bot bot = BotConfig.bot();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        LambdaLogger logger = context.getLogger();

        logger.log("Received request with url =" + input.getPath());

        final String body = input.getBody();

        bot.adaptListener(new MessageSender(bot))
            .onEvent(body);

        logger.log("Successfully handled request");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(201);
        return response;
    }
}