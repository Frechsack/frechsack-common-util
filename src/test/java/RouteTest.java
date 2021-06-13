import frechsack.dev.util.array.Array;
import frechsack.dev.util.route.Route;

import java.util.ArrayList;
import java.util.Collection;

public class RouteTest
{
    public static void main(String[] args)
    {
        Collection<Integer> clt = new ArrayList<>(Array.of(1, 2, 3, 4, 5, 6, 7, 8, 9).asList());

        Route<Integer> route = Route.of(clt.iterator());

        while (route.hasNext()){
            Integer next = route.next();

            if(next == 5){
                route.remove();
                route.previous();
                route.previous();
            }
            System.out.println(next);

        }


        System.out.println(clt);




    }

}
