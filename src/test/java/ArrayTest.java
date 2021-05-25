import com.frechsack.dev.util.array.Array;
import com.frechsack.dev.util.array.NumericArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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

        /*
        ArrayList<Long> primitiveTimeLs = new ArrayList<>(TEST_COUNT);
        ArrayList<Long> listTimeLs = new ArrayList<>(TEST_COUNT);
        ArrayList<Long> newTimeLs = new ArrayList<>(TEST_COUNT);
        ArrayList<Long> oldTimeLs = new ArrayList<>(TEST_COUNT);


        for (int i = 0; i < TEST_COUNT; i++)
        {
            listTimeLs.add(listArray());
            oldTimeLs.add(oldArray());
            newTimeLs.add(newArray());
            primitiveTimeLs.add(primitiveArray());
           // System.gc();

            try
            {
                Thread.sleep(TEST_SLEEP);
                System.out.println("T: " + i);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        // Get Overage & min / max
     //   System.out.println("Prim Min: " + primitiveTimeLs.stream().mapToLong(item -> item).min().getAsLong());
     //   System.out.println("Prim Max: " + primitiveTimeLs.stream().mapToLong(item -> item).max().getAsLong());
        System.out.println("Prim Ovg: " + primitiveTimeLs.stream().mapToLong(item -> item).average().getAsDouble());


        // Get Overage & min / max
     //   System.out.println("List Min: " + listTimeLs.stream().mapToLong(item -> item).min().getAsLong());
     //   System.out.println("List Max: " + listTimeLs.stream().mapToLong(item -> item).max().getAsLong());
        System.out.println("List Ovg: " + listTimeLs.stream().mapToLong(item -> item).average().getAsDouble());

        // Get Overage & min / max
     //   System.out.println("New Min: " + newTimeLs.stream().mapToLong(item -> item).min().getAsLong());
     //   System.out.println("New Max: " + newTimeLs.stream().mapToLong(item -> item).max().getAsLong());
        System.out.println("New Ovg : " + newTimeLs.stream().mapToLong(item -> item).average().getAsDouble());

        // Get Overage & min / max
     //   System.out.println("Old Min: " + oldTimeLs.stream().mapToLong(item -> item).min().getAsLong());
     //   System.out.println("Old Max: " + oldTimeLs.stream().mapToLong(item -> item).max().getAsLong());
        System.out.println("Old Ovg : " + oldTimeLs.stream().mapToLong(item -> item).average().getAsDouble());
    }


    private static long primitiveArray()
    {
        long start = System.nanoTime();
        int[] array = new int[ARRAY_SIZE];
        // fill
        for (int i = 0; i < array.length; i++)
        {
            array[i] = i;
        }
        // Access
        int a;
        for (int i = 0; i < array.length; i++)
        {
            a = array[i];
        }
        // Override
        for (int i = 0; i < array.length; i++)
        {
            array[i] = i;
        }
        long end = System.nanoTime();
        return end - start;
    }


    private static long listArray()
    {
        long start = System.nanoTime();
        List<Integer> list = Arrays.asList(new Integer[ARRAY_SIZE]);
        // fill
        for (int i = 0; i < list.size(); i++)
        {
            list.set(i, i);
        }
        // Access
        int a;
        for (int i = 0; i < list.size(); i++)
        {
            a = list.set(i, i);
        }
        // Override

        for (int i = 0; i < list.size(); i++)
        {
            list.set(i, i);
        }
        long end = System.nanoTime();
        return end - start;
    }

    private static long oldArray()
    {
        long start = System.nanoTime();
        com.frechsack.dev.util.collection.Array<Integer> array = new com.frechsack.dev.util.collection.Array<Integer>(new int[ARRAY_SIZE]);
        // fill
        for (int i = 0; i < array.length(); i++)
        {
            array.set(i,i);
        }
        // Access
        int a;
        for (int i = 0; i < array.length(); i++)
        {
            a = array.getInt(i);
        }
        // Override

        for (int i = 0; i < array.length(); i++)
        {
            array.setInt(i, i);
        }
        long end = System.nanoTime();
        return end - start;

    }

    private static long newArray()
    {
        long start = System.nanoTime();
        NumericArray<Integer> array = Array.ofInt(ARRAY_SIZE);
        // fill
        for (int i = 0; i < array.length(); i++)
        {
            array.set(i,i);
        }
        // Access
        int a;
        for (int i = 0; i < array.length(); i++)
        {
            a = array.getInt(i);
        }
        // Override

        for (int i = 0; i < array.length(); i++)
        {
            array.setInt(i, i);
        }
        long end = System.nanoTime();
        return end - start;

    }
*/
}
