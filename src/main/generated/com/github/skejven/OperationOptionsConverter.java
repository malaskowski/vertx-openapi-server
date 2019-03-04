package com.github.skejven;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter for {@link com.github.skejven.OperationOptions}.
 * NOTE: This class has been automatically generated from the {@link com.github.skejven.OperationOptions} original class using Vert.x codegen.
 */
 class OperationOptionsConverter {

   static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, OperationOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "operationId":
          if (member.getValue() instanceof String) {
            obj.setOperationId((String)member.getValue());
          }
          break;
      }
    }
  }

   static void toJson(OperationOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

   static void toJson(OperationOptions obj, java.util.Map<String, Object> json) {
    if (obj.getOperationId() != null) {
      json.put("operationId", obj.getOperationId());
    }
  }
}
