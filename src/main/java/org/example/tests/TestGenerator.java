package org.example.tests;

import org.example.bean.postman.Collection;
import org.example.bean.postman.Event;
import org.example.bean.postman.Item;

import java.util.ArrayList;
import java.util.List;
public class TestGenerator {
    private Item positive;
    private Item negative;
    private Item mixed;
    private Item testFolder;
    private Collection collection;
    private List<Item> testItems = new ArrayList<>();
    //negative tests
    private List<Item> negativeItems = new ArrayList<>();

    //positive tests
    private List<Item> positiveItems = new ArrayList<>();

    //mixed tests
    private List<Item> mixedItems = new ArrayList<>();

    public TestGenerator(Collection collection){
        //create folder tests
        this.testFolder = new Item("TESTS");

        //create folder for POSITIVE OUTCOME, NEGATIVE OUTCOME and POSITIVE OUTCOME for each method
        this.positive = new Item("POSITIVE OUTCOME");
        this.negative = new Item("NEGATIVE OUTCOME");
        this.mixed = new Item("MIXED OUTCOME");
        this.collection = collection;
    }

    public void generateTests(String val, Item item, String methodName) throws ClassNotFoundException {
        //Request body tests
        generateRequestBodyTests(val, item,  methodName);

        //path tests
        generateRequestPathTests(item);

        //positive tests
        performPositiveTests(item);

        //set mixed tests
        this.mixed.setItem(mixedItems);

        this.negative.setItem(negativeItems);
        this.positive.setItem(positiveItems);

        //add to test folder
        testItems.add(this.mixed);
        testItems.add(this.negative);
        testItems.add(this.positive);

        this.testFolder.setItem(testItems);

        //add to collection
        collection.getItem().add(this.testFolder);
    }

    public void generateRequestBodyTests(String val, Item item, String methodName) throws ClassNotFoundException {

        //TODO: header test
        //TODO: query params test
        //string type tests
        new StringTests().performMixedTests(Class.forName(val).getDeclaredFields(), mixedItems, item);
        new StringTests().performNegativeTests(Class.forName(val).getDeclaredFields(), negativeItems, item);

        //integer type tests

        //boolean type tests

        //decimal type tests

    }


    public void generateRequestPathTests(Item item) {
        //TODO: handle null path
        Item temp = item.copy();
        temp.setName("VERIFY_ENDPOINT_ERROR_WITH_INCORRECT_URL");
        String url = temp.getRequest().getUrl().getRaw();
        temp.getRequest().getUrl().setRaw(url.substring(url.length()-4));

        temp.setEvent(new ArrayList<>());
        temp.getEvent().add(new Event("test", "tobe400"));

        negativeItems.add(temp);
    }

    public void performPositiveTests(Item itemSent){

        String[] positiveTests = new String[]{
                "VERIFY_ENDPOINT_WITH_VALID_URL",
                "VERIFY_ENDPOINT_WITH_VALID_PAYLOAD",
                "VERIFY_ENDPOINT_WITH_VALID_HTTP_CODE"
        };

        for (int i = 0; i < positiveTests.length; i++) {
            Item itemTemp = itemSent.copy();
            itemTemp.setName(positiveTests[i]);

            itemTemp.setEvent(new ArrayList<>());
            itemTemp.getEvent().add(new Event("test", "tobe400"));
            positiveItems.add(itemTemp);
        }

    }

}
