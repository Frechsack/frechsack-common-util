package frechsack.prod.util.stream;

import org.junit.Assert;

import java.util.stream.Stream;

import static org.junit.Assert.*;

public class FStreamTest {

    @org.junit.Test
    public void distinctBy() {

        record Item(String name, int age){}

        Object[] items = FStream.of(Stream.of(
                new Item("A",1),
                new Item("AA",2),
                new Item("AA",3),
                new Item("BB",4),
                new Item("AA",5),
                new Item("BA",6),
                new Item("VA",7),
                new Item("AAA",8))
                ).distinctBy(it -> it.name)
                .toArray();

        Object[] expected = {new Item("A",1),
                new Item("AA",2),
                new Item("BB",4),
                new Item("BA",6),
                new Item("VA",7),
                new Item("AAA",8)};

        Assert.assertArrayEquals(expected, items);

        var items2 = FStream.of(Stream.of(
                        new Item("A",1),
                        new Item("AA",1),
                        new Item("AA",1),
                        new Item("BB",1),
                        new Item("AA",1),
                        new Item("BA",1),
                        new Item("VA",1),
                        new Item("AAA",8))
                ).distinctBy(it -> it.age)
                .toArray();

        Object[] expected2 = {new Item("A",1),
                new Item("AAA",8)};

        Assert.assertArrayEquals(expected2, items2);

    }

    @org.junit.Test
    public void distinct() {
        record Item(String name, int age){}
        Object[] items1 = FStream.of(Stream.of(
                new Item("Name", 12),
                new Item("NameA", 13),
                new Item("NameB", 12),
                new Item("NameC", 14))
        ).distinct((a, b) -> a.age != b.age)
                .toArray();

        Object[] expected1 = {
            new Item("Name", 12),
            new Item("NameA", 13),
            new Item("NameC", 14)
        };

        Assert.assertArrayEquals(expected1, items1);
    }

    @org.junit.Test
    public void sortedBy() {
        record Item(String name, int age){}
        Object[] items1 = FStream.of(Stream.of(
                new Item("Name", 18),
                new Item("NameA", 13),
                new Item("NameB", 19),
                new Item("NameC", 14))
        ).sortedBy(it -> it.age)
                .toArray();

        Object[] expected1 = {new Item("NameA", 13),
                new Item("NameC", 14),
                new Item("Name", 18),
                new Item("NameB", 19)
        };


        Assert.assertArrayEquals(expected1, items1);
    }

    @org.junit.Test
    public void add() {
        record Item(String name, int age){}
        Object[] items1 = FStream.of(Stream.of(
                        new Item("Name", 18),
                        new Item("NameA", 13),
                        new Item("NameB", 19),
                        new Item("NameC", 14))
                ).add(Stream.of(new Item("Name", 18),new Item("NameC", 14)))
                .toArray();
        Object[] expected1 = {
                new Item("Name", 18),
                new Item("NameC", 14),
                new Item("Name", 18),
                new Item("NameA", 13),
                new Item("NameB", 19),
                new Item("NameC", 14),

        };

        Assert.assertArrayEquals(expected1, items1);
    }

    @org.junit.Test
    public void sortedReversed() {
    }

    @org.junit.Test
    public void testSortedReversed() {
    }
}