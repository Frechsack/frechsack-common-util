import frechsack.dev.util.array.Array;

import java.util.*;

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
        Deque<Integer> integers = new ArrayDeque<>();

        integers.offer(1);
        integers.offer(2);
        integers.offer(3);

        integers.poll();

        System.out.println(integers);

    }
}
