package com.jackson.ext.deserializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.jackson.ext.util.UnsafeAllocator;

import java.io.IOException;

public class MyBeanDeserializer extends BeanDeserializer{

    public MyBeanDeserializer(BeanDeserializerBase src) {
        super(src);
    }

    @Override
    public Object deserializeFromObjectUsingNonDefault(JsonParser p,DeserializationContext ctxt) throws IOException{
        JsonDeserializer<Object> delegateDeser = _delegateDeserializer;
        if (delegateDeser == null) {
            delegateDeser = _arrayDelegateDeserializer;
        }
        if (delegateDeser != null) {
            return _valueInstantiator.createUsingDelegate(ctxt,
                    delegateDeser.deserialize(p, ctxt));
        }
        if (_propertyBasedCreator != null) {
            return _deserializeUsingPropertyBased(p, ctxt);
        }

       /*
        // 25-Jan-2017, tatu: We do not actually support use of Creators for non-static
        //   inner classes -- with one and only one exception; that of default constructor!
        //   -- so let's indicate it
       //================================= Unsafe Allocator support Bean instantiate with Non default creator,Include the non-static inner class  =======================
       Class<?> raw = _beanType.getRawClass();
        if (ClassUtil.isNonStaticInnerClass(raw)) {
            return ctxt.handleMissingInstantiator(raw, null, p,
                    "can only instantiate non-static inner class by using default, no-argument constructor");
        }*/
        return _deserializeNonDefaultWithUnsafeAllocator(p,ctxt);
        //return ctxt.handleMissingInstantiator(raw, getValueInstantiator(), p,"cannot deserialize from Object value (no delegate- or property-based Creator)");
    }


    private Object _deserializeNonDefaultWithUnsafeAllocator(JsonParser p, DeserializationContext ctxt) throws IOException{
         Object bean=null;
        try {
            bean = UnsafeAllocator.beanAllocatorByJvmUnsafe(_beanType.getRawClass());
            fillBeanFieldValue(p, ctxt, bean);
        } catch (InstantiationException instException) {
            instException.printStackTrace();
            throw new IOException("MapKeyDeserializer deserializeKey error",instException);
        }
        return bean;
    }

    private void fillBeanFieldValue(JsonParser p, DeserializationContext ctxt, Object bean) throws IOException {
        p.setCurrentValue(bean);
        if (p.hasTokenId(JsonTokenId.ID_FIELD_NAME)) {
            String propName = p.getCurrentName();
            do {
                p.nextToken();
                SettableBeanProperty prop = _beanProperties.find(propName);
                if (prop != null) { // normal case
                    try {
                        prop.deserializeAndSet(p, ctxt, bean);
                    } catch (Exception e) {
                        wrapAndThrow(e, bean, propName, ctxt);
                    }
                    continue;
                }
                handleUnknownVanilla(p, ctxt, bean, propName);
            } while ((propName = p.nextFieldName()) != null);
        }
    }
}