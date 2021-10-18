import frechsack.dev.util.array.Array;
import frechsack.dev.util.array.Numbers;

import java.util.*;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class ArrayTest
{
    public static void main(String[] bca)
    {
        Numbers<?> array = Array.ofLong(10, LongStream.of(1,2,3,4,5));
        System.out.println("array = " + array);
        System.out.println("array.isPrimitive() = " + array.isPrimitive());

        Collections.rotate(array.asList(),2);
        System.out.println(array);
    }
}
