package org.motometer.telegram.bot.aws;

import org.motometer.telegram.bot.aws.service.UpdateListener;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class BotHook implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    var logger = context.getLogger();

    logger.log("Received request with url =" + input.getPath());

    final UpdateListener updateListener = UpdateListener.of(logger);

    updateListener.onUpdate(input.getBody());

    logger.log("Request complete");

    return successResponse();
  }

  private APIGatewayProxyResponseEvent successResponse() {
    var response = new APIGatewayProxyResponseEvent();
    response.setStatusCode(200);
    return response;
  }
}
