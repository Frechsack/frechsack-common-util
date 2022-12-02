package frechsack.prod.util;

import frechsack.prod.util.stream.StreamUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

public class StreamUtilsTest {

    public record Person(String first, String last, int age){}

    @Test
    public void distinct(){
        Stream<Person> personStream = Stream.of(
                new Person("Max", "Musterman", 20 ),
                new Person("Maria", "Musterfrau", 21),
                new Person("Hans", "Gustav", 20),
                new Person("Karl", "Arsch",22)
        );

        Assert.assertArrayEquals(List.of(20,21,22).toArray(), personStream.filter(StreamUtils.distinct((a, b) -> a.age() == b.age()))
                .map(it -> it.age).toArray());
    }

    @Test
    public void distinctBy(){
        Stream<Person> personStream = Stream.of(
                new Person("Max", "Musterman", 20 ),
                new Person("Maria", "Musterfrau", 21),
                new Person("Hans", "Gustav", 20),
                new Person("Karl", "Arsch",22)
        );

        Assert.assertArrayEquals(List.of(20,21,22).toArray(), personStream.filter(StreamUtils.distinctBy(Person::age)).map(it -> it.age).toArray());
    }
}
