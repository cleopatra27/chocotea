# ChocoTea ☕️
⭐ Star on GitHub — it motivates a lot!

Chocotea is a library that generates postman collection, environment and integration tests from java code.

The aim of chocotea is to help reduce the amount of time it takes to setup a postman
collection with requests and tests manually, it runs at compile, generates collection json file 
and works with:

- [Spring](https://mvnrepository.com/artifact/org.springframework/spring-core)
- [Jakarta](https://mvnrepository.com/artifact/jakarta.ws.rs/jakarta.ws.rs-api) 
- [javax](https://mvnrepository.com/artifact/javax.ws.rs/javax.ws.rs-api)

This can be used with more than one controller on any or all the supported libraries, 
however, they need to have different collection names.

## Installation
### Maven
```java
    <dependency>
        <groupId>io.chocotea</groupId>
        <artifactId>chocotea</artifactId>
        <version>1.0</version>
    </dependency>
```

### gradle
Add this dependency to your project's build file:

```java
compile("io.chocotea:chocotea:1.0")
```

## Usage (Annotations)
- [@SpringCollection](documentation/SpringCollection.md)
- [@SpringRequest](documentation/SpringCollection.md)
- [@JakartaCollection](documentation/JakartaCollection.md)
- [@JakartaRequest](documentation/JakartaCollection.md)
- [@JavaxCollection](documentation/JavaxCollection.md)
- [@JavaxRequest](documentation/JavaxRequest.md)
- [@ChocoRandom](documentation/ChocoRandom.md)
- [@ChocoExpect](documentation/ChocoExpect.md)
- [@ChocoDuplicateTest](documentation/ChocoDuplicateTest.md)
- [@ChocoCurrencyTest](documentation/ChocoCurrencyTest.md)

## Example
This example shows a spring collection variables defined
```java
@SpringCollection(
        name = "susan more collection", 
        createTest = true, 
        baseUrl = "https://example.com")
public class PathGenerator {
    
    @SpringRequest(requestBean = Info.class, auth = Auth.Type.bearer, authValue = "ABCTOKEN")
    @PostMapping(value = "/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}")
    public String generate(
            @PathVariable( "resortID" )  int resortID,
            @RequestParam("id") @NotNull @ChocoRandom(dynamic = DynamicVariables.randomCatchPhrase) String id,
            @RequestBody Info resource, 
            @RequestHeader("Authorization") String authorization){
        ....
    }
    
}
```

## Language
This is the list of languages that can be passed to the request. The default is json and options are:
```text
    text,
    json,
    javascript,
    html,
    xml
```

## Auth
This is the list of auth type that can be passed to the request. The default is noauth and options are:
```text
    apikey,
    awsv4,
    basic,
    bearer,
    digest,
    edgegrid,
    hawk,
    noauth,
    oauth1,
    oauth2,
    ntlm
```

This library runs on compile, after which your collection and environment json files are stored in 
your-project-folder/target/generated-sources/annotations

Sample collection [HERE](documentation/sample/sampleCollection.json)
Sample environment [HERE](documentation/sample/sampleEnvironment.json)