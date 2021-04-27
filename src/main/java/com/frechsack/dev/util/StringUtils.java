package com.frechsack.dev.util;

import com.frechsack.dev.util.cursor.IndexRoute;
import com.frechsack.dev.util.cursor.Route;

import java.util.ArrayList;

public class StringUtils
{
    public static void main(String[] args)
    {
        ArrayList<Long> list = new ArrayList<>();
        list.add(1L);
        list.add(2L);
        list.add(3L);
        list.add(4L);
        list.add(5L);
        Route<Long> route = new IndexRoute<>(list::get, list::size);


        while (route.hasNext()){
            System.out.println("route = " + route.next());
        }
        while (route.hasPrevious()){
            System.out.println("route = " + route.previous());
        }

        System.out.println("route = " + route.first());
        System.out.println("Exec");

    }

}
