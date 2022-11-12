package frechsack.prod.util;

import org.junit.Test;
import org.junit.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class ProxyTest {

    @SuppressWarnings("unchecked")
    @Test()
    public void testMap(){

        Map<Integer,String> map = new HashMap<>();

        ProxyBuilder<Map> builder = new ProxyBuilder<>(Map.class)
                .add("put", e -> map.put((Integer) e.getRaw(0), (String) e.getRaw(1)))
                .add("get", e -> map.get((int)e.getRaw(0)));

        Map<Integer,String> proxy = builder.build();
        proxy.put(1,"1");

        Assert.assertEquals("1",proxy.get(1));
    }

    @SuppressWarnings("unchecked")
    @Test()
    public void testSupplier(){

        ProxyBuilder<Supplier> builder = new ProxyBuilder<>(Supplier.class)
                .add("get", e -> 20);

        Supplier<Integer> supplier = builder.build();


        Assert.assertEquals(20,(int)supplier.get());
    }

    @SuppressWarnings("unchecked")
    @Test()
    public void testGeneric(){

        AtomicReference<Integer> result = new AtomicReference<>(0);

        ProxyBuilder<?> builder = new ProxyBuilder<>(IntConsumer.class, Consumer.class)
                .add("accept", e -> {
                    result.set((Integer) e.getRaw(0));
                    return null;
                });

        IntConsumer supA = (IntConsumer) builder.build();
        Consumer supB = (Consumer) builder.build();

        supA.accept(10);
        Assert.assertEquals(10, (int) result.get());


        supB.accept(20);
        Assert.assertEquals(20, (int) result.get());
    }


    @Test
    public void testChained(){
        ProxyBuilder<IntSupplier> builder = new ProxyBuilder<>(IntSupplier.class)
                .add("getAsInt", e -> 20);

        IntSupplier supA = builder.build();
        Assert.assertEquals(20, supA.getAsInt());

        IntSupplier supB = builder.build();
        Assert.assertEquals(20, supB.getAsInt());

        builder.add("getAsInt", e -> 40);
        IntSupplier supC = builder.build();
        Assert.assertEquals(40, supC.getAsInt());

        Assert.assertEquals(20, supB.getAsInt());

        builder.clear();

        IntSupplier supD = builder.build();

        Assert.assertThrows(IllegalStateException.class, supD::getAsInt);
    }
}
