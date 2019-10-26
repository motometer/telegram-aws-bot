package org.motometer.telegram.bot.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.motometer.telegram.bot.WebHookListener;

public class BotHook implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final WebHookListener webHookListener;

    public BotHook() {
        webHookListener = BotConfig.webHookListener();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        LambdaLogger logger = context.getLogger();

        logger.log("Received request with url =" + input.getPath());

        webHookListener.onEvent(input.getBody());

        logger.log("Request complete");

        return successResponse();
    }

    private APIGatewayProxyResponseEvent successResponse() {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(200);
        return response;
    }
}