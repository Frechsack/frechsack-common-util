import frechsack.dev.util.array.Array;
import frechsack.dev.util.array.Booleans;
import frechsack.dev.util.array.Numbers;

import java.util.Collection;

public class ArrayTest
{
    private static final int ARRAY_SIZE = 16394;
    private static final int TEST_COUNT = 4096;
    private static final long TEST_SLEEP = 1L;


    private static long overload(Collection<Long> ls, long l)
    {
        ls.add(l++);
        return l;
    }


    public static void main(String[] bca)
    {
        Booleans a = Array.of(true,true,true,false,true);
        Booleans b = a.resized(8);

        System.out.println(b);
        System.out.println(b.asArray().getClass());


    }
}
