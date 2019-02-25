# SuperEncoder
Specification for ExactPro. Custom serializer via Java.

```java
//JavaBean
MyClass object = new MyClass();
byte[] bytes;

SerializeService service = new SerializeService(MyClass.class);
bytes = service.serialize(object);

MyClass obj = (MyClass) service.deserialize(bytes);
```
