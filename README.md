# Overview

Java生态圈中有很多处理JSON和XML格式化的类库,Jackson是其中比较著名的一个
        官址:https://github.com/FasterXML
Jackson提供了Jackson-databind来操控JSON处理,提供了比较丰富的API,和入门指南 https://github.com/FasterXML/jackson-databind
JAVA是一种灵活性,扩展性比较强的语言,继承,实现,范型,多态等都是很好的体现,Jackson-databind作为JAVA生态圈的类库,当然会对这些特性有对应的实现,具体可以在官方指导文档中,或者是在指导测试中找到相关的介绍。你会发现官方给出的基本上都是基于单个Bean的,或者是某一类型的定制方案。官方暂未提供通用的解决方案,另外对于有些基于JSON数据传输的系统,通过JACKSON进行BEAN的反序列化时,如果没有定制Deserializer行为,Jackson默认的`BeanDeserializer`,此时需要明确此Bean类结构,Bean中的属性类型,枚举以及嵌套类(内部类)中必须显式包含'无参构造器'`(NON-DEFAULT CONSTRUCTOR)`，否则反序列化会失败

-----

# Get it!


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

# Use It!



## 1 minute tutorial: 



## Tutorial:






