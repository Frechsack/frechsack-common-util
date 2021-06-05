import com.frechsack.dev.util.Pair;
import com.frechsack.dev.util.array.Array;
import com.frechsack.dev.util.array.Numbers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

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
        Collection<Integer> numberClt = new ArrayList<>();
        Numbers<Integer> array = Array.ofGenericInt(256,3,6,8,1,9,10,23,54,1,77,8,33);
        numberClt.addAll(array.asList());
        numberClt.addAll(array.asList());

        System.out.println(array);
        System.out.println(array.firstIndexOf(i -> i == 3));
        System.out.println(array);
        System.out.println(Arrays.toString(array.toDoubleArray()));
    }

    private static void print(List<Integer> ls)
    {
        System.out.println(ls);

        ls.set(2, 10);
    }
}
