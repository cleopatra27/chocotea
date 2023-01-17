# ☕️ JavaxRequest
This annotation is used to define methods to be used to generate requests with API(S) built with [javax](https://mvnrepository.com/artifact/javax.ws.rs/javax.ws.rs-api).

## Fields
### name
This is the name of the request, default is name of the method.

### authValue
This is an array with values depending on the auth type.

### requestBean
This is used to define the class if there is a class used for the request.

## Example
```java

@JavaxRequest(requestBean = Info.class, auth = Auth.Type.bearer, authValue = "ABCTOKEN")
@PostMapping(value = "/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}")
    public String generate(
    @PathVariable( "resortID" )  int resortID,
    @RequestParam("id") @NotNull @ChocoRandom(dynamic = DynamicVariables.randomCatchPhrase) String id,
    @RequestBody Info resource,
    @RequestHeader("Authorization") String authorization){
            ....
    }
```