package frechsack.prod.util;

import frechsack.prod.util.collection.HashMultiMap;
import frechsack.prod.util.collection.MultiMap;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class HashMultiMapTest {

    @Test
    public void constructor(){
        new HashMultiMap<>(0);
        new HashMultiMap<>(0,1);

    }

    @Test
    public void write(){
        MultiMap<String, Integer> map = new HashMultiMap<>();
        map.put("A", new ArrayList<>(List.of(1,2,3)));
        map.put("B", new ArrayList<>(List.of(4,5,6)));
        map.put("C", new ArrayList<>(List.of(7,8)));
        map.put("D", new ArrayList<>(List.of(9)));

        Assert.assertEquals(List.of(1,2,3), map.get("A"));
        Assert.assertEquals(0, map.get("E").size());
        Assert.assertEquals(List.of(4,5,6), map.get("B"));
        Assert.assertEquals(List.of(1,2,3,4,5,6,7,8,9), map.values());
        Assert.assertArrayEquals(List.of("A","B","C","D").toArray(), map.keySet().toArray());
        Assert.assertArrayEquals(List.of().toArray(), map.get("FG").toArray());
    }

    @Test
    public void entrySet(){
        MultiMap<String, Integer> map = new HashMultiMap<>();
        map.put("A", new ArrayList<>(List.of(1,2,3)));
        map.put("B", new ArrayList<>(List.of(4,5,6)));
        map.put("C", new ArrayList<>(List.of(7,8)));
        map.put("D", new ArrayList<>(List.of(9)));

        Set<MultiMap.Entry<String, Integer>> entrySet = map.entrySet();

        Assert.assertFalse(entrySet.isEmpty());
        Assert.assertEquals(4, entrySet.size());

        entrySet.add(new MultiMap.Entry<>() {
            @Override
            public String getKey() {
                return "E";
            }

            @Override
            public @NotNull Collection<Integer> get() {
                return List.of(1, 2, 3, 4, 5);
            }
        });

        Assert.assertArrayEquals(List.of(1,2,3,4,5).toArray(), map.get("E").toArray());

        entrySet.stream().filter(it -> it.getKey().equals("B")).forEach(it -> it.add(List.of(7,8,9)));

        Assert.assertArrayEquals(List.of(4,5,6,7,8,9).toArray(), map.get("B").toArray());



    }

}
