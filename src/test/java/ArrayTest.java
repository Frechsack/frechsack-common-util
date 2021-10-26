import frechsack.dev.util.array.Array;
import frechsack.dev.util.array.Booleans;
import frechsack.dev.util.array.Numbers;

import java.util.*;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class ArrayTest {
    public static void main(String[] bca) {
        Booleans array = Array.ofBoolean(false, false, false);
        System.out.println("array = " + array);
        System.out.println("array.isPrimitive() = " + array.isPrimitive());

        PrimitiveIterator.OfLong l;

        array.sort();
        System.out.println(array);
    }
}
