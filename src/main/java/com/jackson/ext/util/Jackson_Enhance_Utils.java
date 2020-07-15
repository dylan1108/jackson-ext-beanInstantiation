package com.jackson.ext.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.jackson.ext.custom.MyBeanDeserializerModifier;
import com.jackson.ext.deserializer.MapKeyDeserializers;
import com.jackson.ext.serializer.MapKeySerializer;

public class Jackson_Enhance_Utils {
    private static final ObjectMapper mapper = new ObjectMapper();
    public static final String CLASS_KEY = "@class";
    public static final String BASE_VALUE_KEY = "\"value\"";
    public static final String BASE_VALUE_NODE_KEY = "value";

    public static String serialize(Object obj) throws RuntimeException {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Throwable var2) {
            throw new RuntimeException(var2);
        }
    }

    public static <T> T deserialize(String jsonString, Class<T> clazz) throws RuntimeException {
        try {
            return mapper.readValue(jsonString, clazz);
        } catch (Throwable var3) {
            throw new RuntimeException(var3);
        }
    }

    public static <T> T deserialize(String jsonString, JavaType type) throws RuntimeException {
        try {
            return mapper.readValue(jsonString, type);
        } catch (Throwable var3) {
            throw new RuntimeException(var3);
        }
    }

    public static JsonNode readNode(String jsonString) throws RuntimeException {
        try {
            return mapper.readTree(jsonString);
        } catch (Throwable var2) {
            throw new RuntimeException(var2);
        }
    }

    public static JsonNode valueToTree(Object o) {
        return mapper.valueToTree(o);
    }

    public static TypeFactory typeFactory() {
        return mapper.getTypeFactory();
    }

    static {
        SimpleModule module = new SimpleModule();
        mapper.enableDefaultTypingAsProperty(ObjectMapper.DefaultTyping.NON_FINAL, "@class");
        module.setKeyDeserializers(new MapKeyDeserializers());
        module.addKeySerializer(Object.class, new MapKeySerializer());
        module.setDeserializerModifier(new MyBeanDeserializerModifier());
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.registerModule(module);
        mapper.setVisibilityChecker(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE));
    }
}