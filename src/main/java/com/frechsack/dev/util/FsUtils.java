package com.frechsack.dev.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FsUtils
{
    private FsUtils() {}

    public static <E> Stream<Pair<Integer, E>> indexStream(Collection<E> clt)
    {
        Iterator<E> iterator = clt.iterator();
        return IntStream.range(0, clt.size()).mapToObj(index -> Pair.of(index, iterator.next()));
    }

}
