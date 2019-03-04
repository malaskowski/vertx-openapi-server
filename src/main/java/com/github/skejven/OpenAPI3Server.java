/*
 * Copyright (C) 2019 Maciej Laskowski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.skejven;

import com.github.skejven.handler.AuthHandlerFactory;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.api.contract.RouterFactoryOptions;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OpenAPI3Server extends AbstractVerticle {

  static final int SERVER_PORT = 8808;
  private static final Logger LOGGER = LoggerFactory.getLogger("OpenAPI3Server");

  private HttpServer server;
  private ServerOptions options;

  @Override
  public void init(Vertx vertx, Context context) {
    super.init(vertx, context);
    options = new ServerOptions(config());
    LOGGER.info("Server initialized with options: " + options);
  }

  public void start(Future<Void> future) {
    LOGGER.info("Configuring OpenAPI3Server");
    OpenAPI3RouterFactory
        .rxCreate(vertx, "routes.yaml")
        .flatMap(routerFactory -> {
          // Spec loaded with success. router factory contains OpenAPI3RouterFactory
          // Set router factory options.
          RouterFactoryOptions factoryOptions = new RouterFactoryOptions()
              .setMountValidationFailureHandler(false)
              .setMountResponseContentTypeHandler(true)
              .setOperationModelKey("openapi_model");
          // Mount the options
          routerFactory.setOptions(factoryOptions);

          // Register security handlers
          final Map<String, AuthHandlerFactory> authHandlerFactoriesByType = loadAuthHandlerFactories();
          options.getSecurityHandlers().forEach(securityHandlerOptions -> {
            registerAuthHandler(routerFactory,
                authHandlerFactoriesByType.get(securityHandlerOptions.getType()),
                securityHandlerOptions);
          });

          // Add an handlers for operations
          options.getOperations().forEach(operationOptions -> {
            addHandlerByOperationId(routerFactory, operationOptions.getOperationId());
          });

          // Now you have to generate the router
          Router router = routerFactory.getRouter();

          // Now you can use your Router instance
          LOGGER.info("Starting OpenAPI3Server at " + SERVER_PORT);
          HttpServer server = vertx
              .createHttpServer(new HttpServerOptions().setPort(SERVER_PORT).setHost("localhost"));
          return server.requestHandler(router).rxListen();
        })
        .subscribe(httpServer -> {
          // Server up and running
          LOGGER.info("Server is running!");
          future.complete();
        }, throwable -> {
          // Error during router factory instantiation or http server start
          LOGGER.error("Failed to start server!");
          future.fail(throwable);
        });

  }

  private void registerAuthHandler(OpenAPI3RouterFactory routerFactory,
      AuthHandlerFactory authHandlerFactory, SecurityHandlerOptions options) {
    if (routerFactory != null) {
      routerFactory.addSecurityHandler(options.getName(),
          authHandlerFactory.create(vertx, options.getConfig()));
    } else {
      throw new IllegalStateException("Factory for " + options + " is not registered!");
    }
  }

  private void addHandlerByOperationId(OpenAPI3RouterFactory routerFactory, String operationId) {
    LOGGER.info("Registering handler for: " + operationId);
    routerFactory.addHandlerByOperationId(operationId, routingContext -> {
      LOGGER.info("Request for " + operationId);
      routingContext.response().setStatusMessage("OK").end();
    });
  }

  private Map<String, AuthHandlerFactory> loadAuthHandlerFactories() {
    List<AuthHandlerFactory> routingFactories = new ArrayList<>();
    ServiceLoader.load(AuthHandlerFactory.class)
        .iterator()
        .forEachRemaining(routingFactories::add);

    LOGGER.info("Auth handler factory types registered: " +
        routingFactories.stream().map(AuthHandlerFactory::getType).collect(Collectors
            .joining(",")));

    return routingFactories.stream()
        .collect(Collectors.toMap(AuthHandlerFactory::getType, Function.identity()));
  }

}