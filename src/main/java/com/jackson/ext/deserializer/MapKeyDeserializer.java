package com.jackson.ext.deserializer;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.jackson.ext.util.Jackson_Enhance_Utils;

import java.io.IOException;

public class MapKeyDeserializer extends KeyDeserializer {
        @Override
        public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
            try {
                JsonNode jsonNode = Jackson_Enhance_Utils.readNode(key);
                JsonNode classNode = jsonNode.get(Jackson_Enhance_Utils.CLASS_KEY);
                Class<?> clz = Class.forName(classNode.asText());
                JsonNode valueNode = jsonNode.get(Jackson_Enhance_Utils.BASE_VALUE_NODE_KEY);
                if (clz.equals(Integer.class)) {
                    return Integer.parseInt(valueNode.asText());
                } else if (clz.equals(Boolean.class)) {
                    return Boolean.parseBoolean(valueNode.asText());
                } else if (clz.equals(Byte.class)) {
                    return Byte.parseByte(valueNode.asText());
                } else if (clz.equals(Short.class)) {
                    return Short.parseShort(valueNode.asText());
                } else if (clz.equals(Long.class)) {
                    return Long.parseLong(valueNode.asText());
                } else if (clz.equals(Float.class)) {
                    return Float.parseFloat(valueNode.asText());
                } else if (clz.equals(Double.class)) {
                    return Double.parseDouble(valueNode.asText());
                } else if (clz.equals(String.class)) {
                    return valueNode.asText();
                } else if (clz.isEnum()) {
                    return Jackson_Enhance_Utils.deserialize(valueNode.toString(), clz);
                } else if (clz.isArray()) {
                    return Jackson_Enhance_Utils.deserialize(valueNode.toString(), clz);
                }
                return Jackson_Enhance_Utils.deserialize(key, clz);
            }catch (Exception e){
                throw new IOException("MapKeyDeserializer deserializeKey error",e);
            }
        }
    }