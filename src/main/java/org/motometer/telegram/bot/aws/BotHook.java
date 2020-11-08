package org.motometer.telegram.bot.aws;

import java.util.ServiceLoader;

import org.motometer.telegram.bot.Bot;
import org.motometer.telegram.bot.aws.service.CommandListener;
import org.motometer.telegram.bot.aws.service.PostgresRepository;
import org.motometer.telegram.bot.aws.service.UpdateListener;
import org.motometer.telegram.bot.aws.service.WebHookListener;
import org.motometer.telegram.bot.client.BotBuilder;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;

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

    final Bot bot = BotBuilder.defaultBuilder()
      .token(System.getenv("TELEGRAM_TOKEN"))
      .apiHost(System.getenv("TELEGRAM_API"))
      .build();

    final WebHookListener updateListener = new ExceptionSafeListener(UpdateListener.of(repository), logger);
    final WebHookListener listener = new ExceptionSafeListener(
      new CommandListener(bot, createGson(), repository, logger),
      logger
    );

    updateListener.onUpdate(input.getBody());
    listener.onUpdate(input.getBody());

    logger.log("Request complete");

    return successResponse();
  }

  private APIGatewayProxyResponseEvent successResponse() {
    var response = new APIGatewayProxyResponseEvent();
    response.setStatusCode(200);
    return response;
  }

  private static Gson createGson() {
    GsonBuilder gsonBuilder = new GsonBuilder();
    for (TypeAdapterFactory factory : ServiceLoader.load(TypeAdapterFactory.class)) {
      gsonBuilder.registerTypeAdapterFactory(factory);
    }
    return gsonBuilder.create();
  }
}
