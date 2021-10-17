import frechsack.dev.util.array.Array;
import frechsack.dev.util.array.Numbers;

import java.util.*;
import java.util.stream.Stream;

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
        Array<Integer> array = Array.ofInt(1,2,3,4,5);
        System.out.println("array = " + array);
        System.out.println("array.isPrimitive() = " + array.isPrimitive());
        array = array.retainAll(Stream.of(1,2,5));
        System.out.println("array = " + array);
        System.out.println("array.isPrimitive() = " + array.isPrimitive());
    }
}
