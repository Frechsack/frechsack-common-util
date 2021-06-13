import com.frechsack.dev.util.array.Array;
import com.frechsack.dev.util.array.Numbers;

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
        Numbers<Double> a = Array.ofDouble(1.0, 2.0, 3.0, 4.0, 5.0);

        Array<Double> b = a.subArray(0, 3);

        System.out.println("A: " + a);
        System.out.println("B: " + b);
    }
}
