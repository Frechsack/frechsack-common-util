import frechsack.dev.util.array.Array;
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
        Numbers<Integer> a = Array.ofTypedNumber(Integer.TYPE,1, 2, 3, 4, 5);
        Numbers<Integer> b = a.subArray(0,3);

        b.sortReverse();

        System.out.println(b.isPrimitive());


        System.out.println(b.equals(a));
        System.out.println("A: " + a);
        System.out.println("B: " + b);
    }
}
