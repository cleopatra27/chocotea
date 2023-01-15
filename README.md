# ChocoTea ☕️
A library that generates postman collection and tests from java code.

The aim of chocotea is to help reduce the amount of time it takes to setup a postman
collection with requests and tests manually.

This works with API(s) built with spring, jakarta and javax.

This library runs at runtime after which generated files are stored in the resources folder or sent to 
the URL provided.

This can be used with more than one controller on any or all the supported libraries, however, they need to have different 
collection names.

## Requirements

## Installation
To install the library,

### Maven

### gradle

## Usage (Annotations)
- [@SpringCollection](documentation/SpringCollection.md)
- [@SpringRequest](documentation/SpringCollection.md)
- [@JavaxCollection](documentation/JavaxCollection.md)
- [@JavaxRequest](documentation/JavaxRequest.md)
- [@ChocoRandom](documentation/ChocoRandom.md)
- [@ChocoDuplicateTest](documentation/ChocoDuplicateTest.md)
- [@ChocoCurrencyTest](documentation/ChocoCurrencyTest.md)

## Language
This is language of the request. The default is json and options are:
```text
    text,
    json,
    javascript,
    html,
    xml
```

## Auth
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


## Sample collection [HERE](documentation/sample/sampleCollection.json)