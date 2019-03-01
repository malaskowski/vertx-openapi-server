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

import io.vertx.core.Future;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.auth.shiro.ShiroAuthOptions;
import io.vertx.ext.web.api.contract.RouterFactoryOptions;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.auth.jwt.JWTAuth;
import io.vertx.reactivex.ext.auth.shiro.ShiroAuth;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;
import io.vertx.reactivex.ext.web.handler.AuthHandler;
import io.vertx.reactivex.ext.web.handler.BasicAuthHandler;
import io.vertx.reactivex.ext.web.handler.JWTAuthHandler;

public class OpenAPI3Server extends AbstractVerticle {

  static final int SERVER_PORT = 8808;
  private static final Logger LOGGER = LoggerFactory.getLogger("OpenAPI3Server");

  private HttpServer server;

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
          // Add an handlers for operations
          addHandlerByOperationId(routerFactory, "listBooks");
          addHandlerByOperationId(routerFactory, "listBooksWithBasicAuth");
          addHandlerByOperationId(routerFactory, "listBooksWithJwtAuth");

          // Add a security handlers
          setupBasicAuth(routerFactory);
          setupJwtAuth(routerFactory);

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

  private void setupBasicAuth(OpenAPI3RouterFactory routerFactory) {
    JsonObject config = new JsonObject().put("properties_path", "classpath:test-auth.properties");
    final ShiroAuth shiroAuth = ShiroAuth.create(vertx, new ShiroAuthOptions().setConfig(config));

    final AuthHandler handler = BasicAuthHandler.create(shiroAuth);
    routerFactory.addSecurityHandler("basicAuthBooks", handler);
  }

  private void setupJwtAuth(OpenAPI3RouterFactory routerFactory) {
    final KeyStoreOptions keyStore = new KeyStoreOptions()
        .setType("jceks")
        .setPath("keystore.jceks")
        .setPassword("secret");
    final JWTAuthOptions config = new JWTAuthOptions().setKeyStore(keyStore);

    final AuthHandler handler = JWTAuthHandler.create(JWTAuth.create(vertx, config));
    routerFactory.addSecurityHandler("jwtAuthBooks", handler);
  }

  private void addHandlerByOperationId(OpenAPI3RouterFactory routerFactory, String operationId) {
    routerFactory.addHandlerByOperationId(operationId, routingContext -> {
      LOGGER.info("Request for {}", operationId);
      routingContext.response().setStatusMessage("OK").end();
    });
  }

}