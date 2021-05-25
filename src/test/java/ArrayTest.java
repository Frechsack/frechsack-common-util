import com.frechsack.dev.util.collection.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayTest
{
    private static final int ARRAY_SIZE = 1024;
    private static final int TEST_COUNT = 32;
    private static final long TEST_SLEEP = 2L;

    public static void main(String[] bca)
    {
        ArrayList<Long> primitiveTimeLs = new ArrayList<>(TEST_COUNT);
        ArrayList<Long> listTimeLs = new ArrayList<>(TEST_COUNT);
        ArrayList<Long> objTimeLs = new ArrayList<>(TEST_COUNT);


        for (int i = 0; i < TEST_COUNT; i++)
        {
            listTimeLs.add(listArray());
            objTimeLs.add(objArray());
            System.gc();
            primitiveTimeLs.add(primitiveArray());
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
        System.out.println("Prim Min: " + primitiveTimeLs.stream().mapToLong(item -> item).min().getAsLong());
        System.out.println("Prim Max: " + primitiveTimeLs.stream().mapToLong(item -> item).max().getAsLong());
        System.out.println("Prim Ovg: " + primitiveTimeLs.stream().mapToLong(item -> item).average().getAsDouble());

        // Get Overage & min / max
        System.out.println("List Min: " + listTimeLs.stream().mapToLong(item -> item).min().getAsLong());
        System.out.println("List Max: " + listTimeLs.stream().mapToLong(item -> item).max().getAsLong());
        System.out.println("List Ovg: " + listTimeLs.stream().mapToLong(item -> item).average().getAsDouble());

        // Get Overage & min / max
        System.out.println("Obj Min: " + objTimeLs.stream().mapToLong(item -> item).min().getAsLong());
        System.out.println("Obj Max: " + objTimeLs.stream().mapToLong(item -> item).max().getAsLong());
        System.out.println("Obj Ovg: " + objTimeLs.stream().mapToLong(item -> item).average().getAsDouble());
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

    private static long objArray()
    {
        long start = System.nanoTime();
        Array<Integer> array = new Array<>(ARRAY_SIZE, Integer.TYPE);
        // fill
        for (int i = 0; i < array.length(); i++)
        {
            array.setInt(i, i);
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

}
