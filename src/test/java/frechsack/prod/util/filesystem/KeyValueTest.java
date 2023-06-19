package frechsack.prod.util.filesystem;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class KeyValueTest {

    @Test
    public void test(){
        String jsonText = "{\"key\":12.34,\"KEY2\":12,\"KEY3\":{\"SUB_KEY\":88, \"ARRAY\": [1  ,  2 ,  3 , 4, false, [ 99 ] ] } }";
        KeyValue<String> json = KeyValue.parseJSON(jsonText);

        Assert.assertEquals(12.34, json.getDouble("key"),0.1);
        Assert.assertEquals(12, json.getInt("KEY2"));

        KeyValue<String> subNode = json.getValues("KEY3", String.class);
        KeyValue<Integer> subNodeArray = subNode.getArray("ARRAY");
        KeyValue<Integer> subSubNodeArray = subNodeArray.getArray(5);

        Assert.assertEquals(88, subNode.getInt( "SUB_KEY"));
        Assert.assertEquals(2, subNodeArray.getInt(1));
        Assert.assertFalse(subNodeArray.getBoolean(4));
        Assert.assertEquals(99, subSubNodeArray.getInt(0));
    }

}