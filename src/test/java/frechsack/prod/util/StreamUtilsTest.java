package frechsack.prod.util;

import frechsack.prod.util.stream.StreamUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtilsTest {

    public record Person(String first, String last, int age){}

    @Test
    public void concat(){
        Object[] result = StreamUtils.concat(
                IntStream.rangeClosed(0,3).boxed(),
                IntStream.rangeClosed(4,6).boxed(),
                IntStream.rangeClosed(7,8).boxed(),
                IntStream.rangeClosed(9,12).boxed()
        ).toArray();

        Assert.assertArrayEquals(
                IntStream.rangeClosed(0,12).boxed().toArray(),
                result
        );

        result = StreamUtils.concat(IntStream.rangeClosed(0,5).boxed()).toArray();

        Assert.assertArrayEquals(
                IntStream.rangeClosed(0,5).boxed().toArray(),
                result
        );
    }


    @Test
    public void multiMapAdd(){

        Assert.assertArrayEquals(
                new Object[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,21,22,23,24,25},
                IntStream.range(0,20).boxed().mapMulti(StreamUtils.mapMultiAdd(Stream.of(21,22,23,24,25))).sorted().toArray()
        );

        Assert.assertArrayEquals(
                new Object[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,21,22,23,24,25},
                IntStream.range(0,20).parallel().boxed().mapMulti(StreamUtils.mapMultiAdd(Stream.of(21,22,23,24,25))).sorted().toArray()
        );
    }

    @Test
    public void sortedBy(){
        Stream<Person> personStream = Stream.of(
                new Person("Max", "Musterman", 20 ),
                new Person("Maria", "Musterfrau", 21),
                new Person("Max", null, 20 ),
                new Person("Hans", "Gustav", 20),
                new Person("Karl", "Arsch",22)
        );

        List<String> lastNames = new ArrayList<>();
        lastNames.add("Arsch");
        lastNames.add("Gustav");
        lastNames.add("Musterfrau");
        lastNames.add("Musterman");
        lastNames.add(null);


        Assert.assertArrayEquals(
                lastNames.toArray(),
                personStream.sorted(StreamUtils.sortedBy(it -> it.last))
                        .map(it -> it.last)
                        .toList()
                        .toArray());

    }

    @Test
    public void filterDistinct(){
        Stream<Person> personStream = Stream.of(
                new Person("Max", "Musterman", 20 ),
                new Person("Maria", "Musterfrau", 21),
                new Person("Hans", "Gustav", 20),
                new Person("Karl", "Arsch",22)
        );

        Assert.assertArrayEquals(List.of(20,21,22).toArray(), personStream.filter(StreamUtils.filterDistinct((a, b) -> a.age() == b.age()))
                .map(it -> it.age).toArray());
    }

    @Test
    public void filterDistinctBy(){
        Stream<Person> personStream = Stream.of(
                new Person("Max", "Musterman", 20 ),
                new Person("Maria", "Musterfrau", 21),
                new Person("Hans", "Gustav", 20),
                new Person("Karl", "Arsch",22)
        );

        Assert.assertArrayEquals(List.of(20,21,22).toArray(), personStream.filter(StreamUtils.filterDistinctBy(Person::age)).map(it -> it.age).toArray());
    }
}
