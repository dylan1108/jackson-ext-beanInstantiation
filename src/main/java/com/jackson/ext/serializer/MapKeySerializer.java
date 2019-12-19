package com.jackson.ext.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.jackson.ext.util.Jackson_Enhance_Utils;

import java.io.IOException;
import java.lang.reflect.Modifier;

public class MapKeySerializer extends JsonSerializer {
        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            String keyString = Jackson_Enhance_Utils.serialize(value);
            boolean isFinal = Modifier.isFinal(value.getClass().getModifiers());
            StringBuilder key = new StringBuilder();
            if (isFinal) {

                key.append("{\"").append(Jackson_Enhance_Utils.CLASS_KEY).append("\":\"").append(value.getClass().getName()).append("\",");

                if (value instanceof Integer || value instanceof Boolean
                        || value instanceof Byte || value instanceof Short
                        || value instanceof Long || value instanceof Float
                        || value instanceof Double || value instanceof String) {
                    key.append(Jackson_Enhance_Utils.BASE_VALUE_KEY).append(":").append(keyString);
                } else if (value instanceof Enum) {
                    key.append(Jackson_Enhance_Utils.BASE_VALUE_KEY).append(":").append(keyString);
                } else if(value.getClass().isArray()){
                    key.append(Jackson_Enhance_Utils.BASE_VALUE_KEY).append(":").append(keyString);
                } else if (keyString.startsWith("{")) {
                    key.append(keyString.substring(1, keyString.length() - 1));
                } else {
                    key.append(keyString);
                }

                key.append("}");
                keyString = key.toString();
            }
            gen.writeFieldName(keyString);
        }
    }