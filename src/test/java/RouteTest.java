import frechsack.dev.util.array.Array;
import frechsack.dev.util.route.BiIterator;

import java.util.ArrayList;
import java.util.Collection;

public class RouteTest
{
    public static void main(String[] args)
    {
        Collection<Integer> clt = new ArrayList<>(Array.of(1, 2, 3, 4, 5, 6, 7, 8, 9).asList());

        BiIterator<Integer> biIterator = BiIterator.of(clt.iterator());

        while (biIterator.hasNext()){
            Integer next = biIterator.next();

            if(next == 5){
                biIterator.remove();
                biIterator.previous();
                biIterator.previous();
            }
            System.out.println(next);

        }


        System.out.println(clt);




    }

}
