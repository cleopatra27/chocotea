# ☕️ SpringCollection
This annotation is to be used together with `@SpringRequest` for API(S) written using [Spring](https://mvnrepository.com/artifact/org.springframework/spring-core).

## Fields
### name
This is the name of the collection, default is "Sample Collection".

### createTest
This is a boolean value to enable creating tests for the requests or not, default is false;

### baseUrl
This is the base url that should be used across the collection, default is "{{BASE_URL}}"

## Example
This example shows a spring collection variables defined
```java
@SpringCollection(
        name = "susan more collection", 
        createTest = true, 
        baseUrl = "https://example.com")
public class PathGenerator {
    
    ...
    
}
```
