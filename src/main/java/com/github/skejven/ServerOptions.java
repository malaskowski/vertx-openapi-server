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

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import java.util.Collections;
import java.util.List;

@DataObject(generateConverter = true, publicConverter = false)
public class ServerOptions {

  private String routes;
  private List<OperationOptions> operations;
  private List<SecurityHandlerOptions> securityHandlers;

  public ServerOptions(JsonObject config) {
    this.operations = Collections.emptyList();
    this.securityHandlers = Collections.emptyList();
    ServerOptionsConverter.fromJson(config, this);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    ServerOptionsConverter.toJson(this, json);
    return json;
  }

  public String getRoutes() {
    return routes;
  }

  public ServerOptions setRoutes(String routes) {
    this.routes = routes;
    return this;
  }

  public List<OperationOptions> getOperations() {
    return operations;
  }

  public ServerOptions setOperations(List<OperationOptions> operations) {
    this.operations = operations;
    return this;
  }

  public List<SecurityHandlerOptions> getSecurityHandlers() {
    return securityHandlers;
  }

  public ServerOptions setSecurityHandlers(
      List<SecurityHandlerOptions> securityHandlers) {
    this.securityHandlers = securityHandlers;
    return this;
  }

  @Override
  public String toString() {
    return this.toJson().encodePrettily();
  }
}
