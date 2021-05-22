import com.frechsack.dev.util.collection.Array;

import java.util.Arrays;
import java.util.List;

public class ArrayTest
{
    public static void main(String[] bca)
    {
        final Array<String> stringArray = new Array<>(5,String.class);
        stringArray.set(0,"A");
        stringArray.set(1,"B");


        List<String> stringList = stringArray.list();
        stringList.remove("A");

        String[] clone = new String[stringArray.length()];
        if(clone == stringList.toArray(clone)){
            // Copy worked!
            System.out.println("Copy worked!");
        }

        System.out.println(Arrays.toString(clone));

    }

}
