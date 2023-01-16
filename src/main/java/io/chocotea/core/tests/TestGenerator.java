package io.chocotea.core.tests;

import io.chocotea.bean.postman.Event;
import io.chocotea.bean.postman.Item;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.chocotea.core.tests.TestGenerator.PostmanVerify.postmanVerifyStatusCode;

public class TestGenerator {
    private Item positive;
    private Item negative;
    private Item mixed;
    private List<Item> testItems;
    private List<Item> negativeItems = new ArrayList<>();
    private List<Item> positiveItems = new ArrayList<>();
    private List<Item> mixedItems = new ArrayList<>();

    static abstract class PostmanVerify{
        public static String postmanVerifyStatusCode(int code){
            return "pm.test(\"Status code is "+code+"\", function () {\n" +
                    "    pm.response.to.have.status("+code+");\n" +
                    "});";
        }

        public String postmanVerifyResponseBody(Map<String, Object> expect){
            //TODO pm.response.code:Number, pm.response.status:String, pm.response.headers:HeaderList,
            // pm.response.responseTime:Number, pm.response.responseSize:Number, pm.response.text():Function → String

            final String[] val = new String[1];
            val[0] =  "pm.test(\"Validate response body contains valid response\",  function () {\n" +
                    "    var jsonData = pm.response.json();\n";

            expect.forEach((key, value) -> val[0] = val[0] + " pm.expect(jsonData." + key
                    + ").to.be.a(\"" + value + "\");  \n" +
                    "    pm.expect(jsonData.success).to.equal(true);\n" +
                    "});");

            return val[0];
        }
    }

    public TestGenerator(List<Item> testItems){
        this.testItems = testItems;

        //create folder for POSITIVE OUTCOME, NEGATIVE OUTCOME and POSITIVE OUTCOME for each method
        this.positive = new Item("POSITIVE OUTCOME");
        this.negative = new Item("NEGATIVE OUTCOME");
        this.mixed = new Item("MIXED OUTCOME");
    }

    public void generate(TypeMirror requestMirror, TypeMirror responseMirror, Item item) {

        //body tests
        try {
            generateRequestBodyTests(requestMirror, responseMirror, item);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        //path tests
        generateRequestPathTests(item);

        //positive tests
        generatePositiveTests(item);

        //set mixed tests
        this.mixed.setItem(mixedItems);

        this.negative.setItem(negativeItems);
        this.positive.setItem(positiveItems);

        //add to test folder
        testItems.add(this.mixed);
        testItems.add(this.negative);
        testItems.add(this.positive);

    }

    public void generateRequestBodyTests(TypeMirror requestMirror, TypeMirror responseMirror, Item item) throws ClassNotFoundException {

        //TODO: header test
        //TODO: query params test

        //string type tests
        new BodyTests().performTests(
                (((DeclaredType) requestMirror).asElement()).getEnclosedElements(),
                (((DeclaredType) responseMirror).asElement()).getEnclosedElements(),mixedItems, negativeItems, item
        );



    }


    private void generateRequestPathTests(Item item) {
        Item temp = item.copy();
        temp.setName("VERIFY_ENDPOINT_ERROR_WITH_INCORRECT_URL");
        String url = temp.getRequest().getUrl().getRaw();
        temp.getRequest().getUrl().setRaw(url.substring(url.length()-4));

        temp.setEvent(new ArrayList<>());
        temp.getEvent().add(new Event("test", postmanVerifyStatusCode(404)));

        negativeItems.add(temp);
    }

    private void generatePositiveTests(Item itemSent){

        String[] positiveTests = new String[]{
                "VERIFY_ENDPOINT_WITH_VALID_URL",
                "VERIFY_ENDPOINT_WITH_VALID_PAYLOAD",
                "VERIFY_ENDPOINT_WITH_VALID_HTTP_CODE"
        };

        for (int i = 0; i < positiveTests.length; i++) {
            Item itemTemp = itemSent.copy();
            itemTemp.setName(positiveTests[i]);

            itemTemp.setEvent(new ArrayList<>());
            itemTemp.getEvent().add(new Event("test", postmanVerifyStatusCode(200)));
            positiveItems.add(itemTemp);
        }

    }

}
