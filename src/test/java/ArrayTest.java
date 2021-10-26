import frechsack.dev.util.array.Array;
import frechsack.dev.util.array.Booleans;
import frechsack.dev.util.array.Numbers;
import frechsack.dev.util.route.BiIterator;

import java.util.*;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class ArrayTest {
    public static void main(String[] bca) {
        Numbers<Integer> array = Array.ofInt(1, 2, 3, 4, 5, 6, 7, 8, 9);
        System.out.println("array = " + array);
        System.out.println("array.isPrimitive() = " + array.isPrimitive());

        BiIterator<Integer> iterator = array.biIterator();

        while (iterator.hasNext()) {
            if (iterator.hasPrevious()) {
                System.out.println("Prev: " + iterator.previous());
                iterator.next();
            }
            System.out.print("Next: ");
            System.out.println(iterator.next());
        }
        System.out.println("ForEachPrev");
        iterator.forEachPrevious(System.out::println);
    }
}
