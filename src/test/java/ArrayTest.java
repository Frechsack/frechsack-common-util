import com.frechsack.dev.util.array.AbstractArray;
import com.frechsack.dev.util.array.Array;
import com.frechsack.dev.util.route.Route;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.IntFunction;
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


    public static void equalTest()
    {

        Array<Boolean> a = Array.ofBoolean(false,true,false,true,false);
        Array<Boolean> b = Array.ofBoolean(true,true,false,false,false);

         a.sort();
         b.sort();
         b.reverse();


        a.stream().forEach(e -> System.out.print(e + ","));
        System.out.println();
        b.stream().forEach(e -> System.out.print(e + ","));
        System.out.println();

        System.out.println(a);
        System.out.println(b);
        System.out.println("Equals: " + a.equals(b));

    }

    public static void main(String[] bca)
    {
        equalTest();
    }
}
