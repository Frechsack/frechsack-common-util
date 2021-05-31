import com.frechsack.dev.util.array.Array;
import com.frechsack.dev.util.array.Numbers;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ArrayTest
{
    private static final int ARRAY_SIZE = 16394;
    private static final int TEST_COUNT = 4096;
    private static final long TEST_SLEEP = 1L;


    private static long overload(Collection<Long> ls, long l){
        ls.add(l++);
        return l;
    }

    public static void main(String[] bca)
    {
        int[] ints = new int[]{1,2,3,4,5,6,7,8,9,10};
        print(Array.ofInt(ints).asList());
        System.out.println(Arrays.toString(ints));
    }

    private static void print(List<Integer> ls){
        System.out.println(ls);

        ls.set(2,10);
    }
}
