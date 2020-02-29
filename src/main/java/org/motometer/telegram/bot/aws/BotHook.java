package org.motometer.telegram.bot.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.util.Collection;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.motometer.telegram.bot.core.api.WebHookListener;

public class BotHook implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  private final Collection<WebHookListener> listeners;

  public BotHook() {
    final var load = ServiceLoader.load(WebHookListener.class);
    listeners = StreamSupport.stream(load.spliterator(), false).collect(Collectors.toList());
  }

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    var logger = context.getLogger();

    logger.log("Received request with url =" + input.getPath());

    listeners.forEach(listener -> listener.onEvent(input.getBody()));

    logger.log("Request complete");

    return successResponse();
  }

  private APIGatewayProxyResponseEvent successResponse() {
    var response = new APIGatewayProxyResponseEvent();
    response.setStatusCode(200);
    return response;
  }
}
