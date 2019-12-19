# Overview

jackson在进行对象的反序列化时要求该对象以及对象内的对象类型引用(包括枚举类型）必须存在无参的构造器，否则无法正常的进行反序列化,<br>
此约束限制了RD的编程习惯,多数情况下,RD提供有参构造器覆盖了无参默认的构造器之后,在此情况下,无法正常的进行反序列化,如果有项目使用<br>
Jackson-databind进行Json数据传输的时候,或是是基于REST服务提供的时候,需要遵循此限制

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



## 1 minute tutorial: POJOs to JSON and back



## Tutorial:





-----



## Support

### Community support



