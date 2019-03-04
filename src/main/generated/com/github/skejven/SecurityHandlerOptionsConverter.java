package com.github.skejven;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter for {@link com.github.skejven.SecurityHandlerOptions}.
 * NOTE: This class has been automatically generated from the {@link com.github.skejven.SecurityHandlerOptions} original class using Vert.x codegen.
 */
 class SecurityHandlerOptionsConverter {

   static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, SecurityHandlerOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "config":
          if (member.getValue() instanceof JsonObject) {
            obj.setConfig(((JsonObject)member.getValue()).copy());
          }
          break;
        case "name":
          if (member.getValue() instanceof String) {
            obj.setName((String)member.getValue());
          }
          break;
        case "type":
          if (member.getValue() instanceof String) {
            obj.setType((String)member.getValue());
          }
          break;
      }
    }
  }

   static void toJson(SecurityHandlerOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

   static void toJson(SecurityHandlerOptions obj, java.util.Map<String, Object> json) {
    if (obj.getConfig() != null) {
      json.put("config", obj.getConfig());
    }
    if (obj.getName() != null) {
      json.put("name", obj.getName());
    }
    if (obj.getType() != null) {
      json.put("type", obj.getType());
    }
  }
}
