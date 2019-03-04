package com.github.skejven;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter for {@link com.github.skejven.ServerOptions}.
 * NOTE: This class has been automatically generated from the {@link com.github.skejven.ServerOptions} original class using Vert.x codegen.
 */
 class ServerOptionsConverter {

   static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, ServerOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "operations":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<com.github.skejven.OperationOptions> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof JsonObject)
                list.add(new com.github.skejven.OperationOptions((JsonObject)item));
            });
            obj.setOperations(list);
          }
          break;
        case "routes":
          if (member.getValue() instanceof String) {
            obj.setRoutes((String)member.getValue());
          }
          break;
        case "securityHandlers":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<com.github.skejven.SecurityHandlerOptions> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof JsonObject)
                list.add(new com.github.skejven.SecurityHandlerOptions((JsonObject)item));
            });
            obj.setSecurityHandlers(list);
          }
          break;
      }
    }
  }

   static void toJson(ServerOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

   static void toJson(ServerOptions obj, java.util.Map<String, Object> json) {
    if (obj.getOperations() != null) {
      JsonArray array = new JsonArray();
      obj.getOperations().forEach(item -> array.add(item.toJson()));
      json.put("operations", array);
    }
    if (obj.getRoutes() != null) {
      json.put("routes", obj.getRoutes());
    }
    if (obj.getSecurityHandlers() != null) {
      JsonArray array = new JsonArray();
      obj.getSecurityHandlers().forEach(item -> array.add(item.toJson()));
      json.put("securityHandlers", array);
    }
  }
}
