package org.motometer.telegram.bot.aws;

import org.motometer.telegram.bot.aws.service.PostgresRepository;
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

    final PostgresRepository repository = new PostgresRepository(
      System.getenv("JDBC_URL"),
      System.getenv("DB_USERNAME"),
      System.getenv("DB_PASSWORD")
    );

    final UpdateListener updateListener = UpdateListener.of(logger, repository);

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
