# ☕️ ChocoExpect

This can be used to define fields to be used for tests

## Example
```java
public class testResponse {

    @ChocoExpect
    private String message;

    @ChocoExpect
    private boolean success;

    private Data data;
}
```