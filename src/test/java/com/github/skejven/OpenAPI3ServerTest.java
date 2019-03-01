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

import static com.github.skejven.OpenAPI3Server.SERVER_PORT;
import static io.restassured.RestAssured.given;

import io.knotx.junit5.KnotxApplyConfiguration;
import io.knotx.junit5.KnotxExtension;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.jwt.JWTOptions;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.auth.jwt.JWTAuth;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(KnotxExtension.class)
@KnotxApplyConfiguration("server.conf")
class OpenAPI3ServerTest {

  @Test
  @DisplayName("Expect Ok - call public endpoint")
  void getBooks(Vertx vertx) {
    // @formatter:off
    given()
        .port(SERVER_PORT)
      .when()
        .get("/books")
      .then()
        .assertThat().statusCode(200);
    // @formatter:on
  }

  @Test
  @DisplayName("Expect Unauthorized - call protected endpoint")
  void getBookNoAuth(Vertx vertx) {
    // @formatter:off
    given()
        .port(SERVER_PORT)
      .when()
        .get("/protected/ba")
      .then()
        .assertThat().statusCode(401);
    // @formatter:on
  }

  @Test
  @DisplayName("Expect OK - call protected endpoint with basic auth")
  void getBookWithBasicAuth(Vertx vertx) {
    // @formatter:off
    given()
        .header("Authorization", "Basic am9objpzM2NyM3Q=")
        .port(SERVER_PORT)
      .when()
        .get("/protected/ba")
      .then()
        .assertThat().statusCode(200);
    // @formatter:on
  }

  @Test
  @DisplayName("Expect OK - call protected endpoint with JWT auth")
  void getBookWithJWTAuth(Vertx vertx) {
    // @formatter:off
    given()
        .header("Authorization", generateToken(vertx))
        .port(SERVER_PORT)
      .when()
        .get("/protected/jwt")
      .then()
        .assertThat().statusCode(200);
    // @formatter:on
  }

  private String generateToken(Vertx vertx) {
    final KeyStoreOptions keyStore = new KeyStoreOptions()
        .setType("jceks")
        .setPath("keystore.jceks")
        .setPassword("secret");
    final JWTAuthOptions config = new JWTAuthOptions().setKeyStore(keyStore);

    final JWTAuth jwtAuth = JWTAuth.create(vertx, config);
    final String token = jwtAuth
        .generateToken(new JsonObject().put("name", "john"), new JWTOptions().setExpiresInSeconds(60));
    return "Bearer " + token;
  }

}