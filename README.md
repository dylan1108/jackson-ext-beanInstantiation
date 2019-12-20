# Overview

Java生态圈中有很多处理JSON和XML格式化的类库,Jackson是其中比较著名的一个<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;官址:https://github.com/FasterXML<br>
Jackson提供了Jackson-databind来操控JSON处理,提供了比较丰富的API,和入门指南<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;文档:https://github.com/FasterXML/jackson-databind <br>
JAVA是一种灵活性,扩展性比较强的语言,继承,实现,范型,多态等都是很好的体现,Jackson-databind作为JAVA生态圈的类库,当然会对这些特性有对应的实现,具体可以在官方指导文档中,或者是在指导测试中找到相关的介绍。

-----

# 问题

## Bean多态反序列化
你会发现官方给出的基本上都是基于单个Bean的,或者是某一类型的定制方案。官方暂未提供通用的解决方案,
## 默认Bean反序列化约束
对于有些基于JSON数据传输的系统,通过JACKSON进行BEAN的反序列化时,如果没有定制Deserializer行为,Jackson默认的`BeanDeserializer`,此时需要明确此Bean类结构,Bean中的属性类型,枚举以及嵌套类(内部类)中必须显式包含'无参构造器'`(NON-DEFAULT CONSTRUCTOR)`,否则反序列化会失败,具体异常描述`"No suitable constructor found for type [XXXX]"`

# 方案解决
针对上述的2个问题，`jackson-ext-beanInstantiation`提供解决方案,并且提供基于jackson-databind `simple module`的集成方案

## Bean多态反序列化
Bean序列化时,对于含有多态类型的引用,将此引用的具体实例对象类的信息作为辅助信息,以Key-Value的方式保存到JNode中
Bean反序列化时,会根据JNode中的引用类型的类的辅助信息,来决定使用具体的类来进行反序列化绑定。

## 无参构造Bean反序列化定制
#### 无参构造Bean实例化
`JVM unsafe mechanism`机制无需依赖构造器的方式实例化对象,然后在对Bean对象进行属性设置
```java
public class UnsafeAllocator {
    private static Unsafe unsafe;
    static{
        try {
            Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            Field f = unsafeClass.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }
    public static Object beanAllocatorByJvmUnsafe(Class<?> clazz) throws InstantiationException {
        if(unsafe==null){
            throw new InstantiationException(String.format("Jvm Unsafe could't find,Make sure load unsafe security in [%s]",clazz.getName()));
        }
        return unsafe.allocateInstance(clazz);
    }
}
```

#### 重写BeanDeserializer.deserializeFromObjectUsingNonDefault

```java

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
       //=== Unsafe Allocator support Bean instantiate with Non default creator,Include the non-static inner class =====
       Class<?> raw = _beanType.getRawClass();
        if (ClassUtil.isNonStaticInnerClass(raw)) {
            return ctxt.handleMissingInstantiator(raw, null, p,
                    "can only instantiate non-static inner class by using default, no-argument constructor");
        }*/
        return _deserializeNonDefaultWithUnsafeAllocator(p,ctxt);
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
    
```

#### SimpleModule集成

```java
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
```

## 测试示例

```json
{
	"@class": "com.jackson.ext.test.vo.MockBizDataContext",
	"bizDataMap": {
		"@class": "java.util.HashMap",
		"{\"@class\":\"java.lang.String\",\"value\":\"MockPojoWithNonDefaultConstructorList\"}": ["java.util.ArrayList", [{
			"@class": "com.jackson.ext.test.vo.MockPojoWithNonDefaultConstructor",
			"poiId": 123,
			"comment": "test1",
			"userName": "yilami1",
			"phone": "11111111"
		}, {
			"@class": "com.jackson.ext.test.vo.MockPojoWithNonDefaultConstructor",
			"poiId": 234,
			"comment": "test2",
			"userName": "yilami2",
			"phone": "22222222"
		}]],
		"{\"@class\":\"java.lang.String\",\"value\":\"MockPojoWithNonStaticInnerPojo_Outer\"}": {
			"@class": "com.jackson.ext.test.vo.MockPojoWithNonStaticInnerPojo_Outer",
			"outerName": "Outer[1]",
			"mockPojoWithNonStaticInnerPojo_inner": {
				"@class": "com.jackson.ext.test.vo.MockPojoWithNonStaticInnerPojo_Outer$MockPojoWithNonStaticInnerPojo_Inner",
				"innerName": "Inner[1]"
			}
		},
		"{\"@class\":\"java.lang.String\",\"value\":\"createTime\"}": ["java.util.Date", 1576838321729],
		"{\"@class\":\"java.lang.String\",\"value\":\"MockPojoWithNonDefaultConstructor1\"}": {
			"@class": "com.jackson.ext.test.vo.MockPojoWithNonDefaultConstructor",
			"poiId": 123,
			"comment": "test1",
			"userName": "yilami1",
			"phone": "11111111"
		},
		"{\"@class\":\"java.lang.String\",\"value\":\"MockPojoWithNonDefaultConstructor2\"}": {
			"@class": "com.jackson.ext.test.vo.MockPojoWithNonDefaultConstructor",
			"poiId": 234,
			"comment": "test2",
			"userName": "yilami2",
			"phone": "22222222"
		},
		"{\"@class\":\"java.lang.String\",\"value\":\"MockEnumWithNonDefaultCreator\"}": ["com.jackson.ext.test.Enum.MockEnumWithNonDefaultCreator", "NO"],
		"{\"@class\":\"java.lang.String\",\"value\":\"MockPojoWithNonStaticInnerPojo_Inner\"}": {
			"@class": "java.lang.Object",
			"innerName": "Inner[1]"
		}
	}
}

```

## Maven


```xml
<properties>
  ...
  <!-- Use the latest version whenever possible. -->
  <jackson.version>2.10.0</jackson.version>
  ...
</properties>

<dependencies>
  ...
  <dependency>
    <groupId>com.jackson.ext</groupId>
    <artifactId>jackson-ext-beanInstantiation</artifactId>
    <version>${jackson.version}</version>
  </dependency>
  ...
</dependencies>
```

-----

# 如何使用!
使用Maven编译和打包jackson-ext-beanInstantiation之后，使用Jackson_Enhance_Utils会提供想对应的序列化和反序列化接口即可

# Note!
`sun.misc.Unsafe`的JDK版本的支持



