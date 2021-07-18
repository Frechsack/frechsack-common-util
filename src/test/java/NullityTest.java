import frechsack.dev.util.array.Array;

import static frechsack.dev.util.Nullity.*;
public class NullityTest
{
    public static void main(String[] args)
    {
        Object a = null;
        Object b = "value";

        b = null;


        System.out.println(containsNonNull(Array.of(null,null).asList()));

    }
}
