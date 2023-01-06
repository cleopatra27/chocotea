# ☕️ SpringRequest
This annotation is used to define methods to be used to generate requests with API(S) built with springboot.

## Methods

### name
This is the name of the request, default is name of the method.

### language
This is language of the request. The default is json and options are:
```text
    text,
    json,
    javascript,
    html,
    xml
```

### auth
This is used to define the auth type for the request. The default is noauth and options are:
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

### authValue
This is an array with values depending on the auth type.

### requestBean
This is used to define the class if there is a class used for the request.


## Example
```java
@SpringCollection(
        name = "susan more collection", 
        createTest = true, 
        language = json,
        baseUrl = "https://example.com")
```