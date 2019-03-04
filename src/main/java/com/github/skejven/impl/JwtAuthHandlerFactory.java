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
package com.github.skejven.impl;

import com.github.skejven.handler.AuthHandlerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.auth.jwt.JWTAuth;
import io.vertx.reactivex.ext.web.handler.AuthHandler;
import io.vertx.reactivex.ext.web.handler.JWTAuthHandler;

public class JwtAuthHandlerFactory implements AuthHandlerFactory {

  @Override
  public String getType() {
    return "JWT";
  }

  @Override
  public AuthHandler create(Vertx vertx, JsonObject config) {
    final KeyStoreOptions keyStoreOptions = new KeyStoreOptions(config);
    final JWTAuthOptions jwtAuthOptions = new JWTAuthOptions().setKeyStore(keyStoreOptions);
    return JWTAuthHandler.create(JWTAuth.create(vertx, jwtAuthOptions));
  }
}
