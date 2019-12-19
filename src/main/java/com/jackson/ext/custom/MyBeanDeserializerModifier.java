package com.jackson.ext.custom;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.jackson.ext.deserializer.MyBeanDeserializer;

public class MyBeanDeserializerModifier  extends BeanDeserializerModifier {
        @Override
        public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
            //replace default BeanDeserializer to support instantiate bean with Non-default creator.
            if (deserializer instanceof BeanDeserializer) {
                return new MyBeanDeserializer((BeanDeserializer) deserializer);
            }
            return deserializer;
        }
    }