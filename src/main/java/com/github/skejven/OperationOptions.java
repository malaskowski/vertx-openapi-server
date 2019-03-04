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

@DataObject(generateConverter = true, publicConverter = false)
public class OperationOptions {

  private String operationId;

  public OperationOptions(JsonObject config) {
    OperationOptionsConverter.fromJson(config, this);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    OperationOptionsConverter.toJson(this, json);
    return json;
  }

  public String getOperationId() {
    return operationId;
  }

  public OperationOptions setOperationId(String operationId) {
    this.operationId = operationId;
    return this;
  }

  @Override
  public String toString() {
    return this.toJson().encodePrettily();
  }
}
