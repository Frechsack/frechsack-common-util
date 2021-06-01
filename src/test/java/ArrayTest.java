import com.frechsack.dev.util.array.Array;
import com.frechsack.dev.util.array.Numbers;

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
        IntStream.Builder builder = IntStream.builder();
        IntStream.range(0,4100).forEach(builder::add);
        for (int i = 0; i < 10; i++) builder.add(2);




        int[] array  = builder.build().toArray();
        Numbers<Integer> numbers = Array.ofInt(array);

        System.out.println(array.length);

        long start = System.nanoTime();

        System.out.println(numbers.lastIndexOf(2));

        System.out.println(System.nanoTime() - start);
    }

    private static void print(List<Integer> ls)
    {
        System.out.println(ls);

        ls.set(2, 10);
    }
}
