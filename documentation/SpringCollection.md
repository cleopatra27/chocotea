# ☕️ SpringCollection

This annotation is to be used together with @SpringRequest for API(S) written using Springboot.

## Methods

### name
This is the name of the collection, default is "Sample Collection".

### createTest
This is a boolean value to enable creating tests for the requests or not, default is false;

### baseUrl
This is the base url that should be used across the collection, default is "{{BASE_URL}}"

### jsonPath

### protocol

## Examples

- This example shows a spring collection variables defined
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
