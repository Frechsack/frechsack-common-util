package frechsack.dev.util.signal;

import frechsack.prod.util.concurrent.flow.CompactSubscriber;
import org.junit.Assert;
import org.junit.Test;

public class WriteableGenericSignalTest {

    @Test
    public void set() throws InterruptedException {
        WriteableObjectSignal<Integer> root = new WriteableObjectSignal<>(null, null);
        WriteableObjectSignal<Boolean> isSet = new WriteableObjectSignal<>(false, null);
        root.subscribeOnChange(new CompactSubscriber<>(Long.MAX_VALUE) {
            @Override
            public void onNext(Integer item) {
                isSet.set(true);
            }
        });
        Assert.assertFalse(isSet.get());

        root.set(10);
        Thread.sleep(15);
        Assert.assertTrue(isSet.get());
    }

    @Test
    public void get() throws InterruptedException {
        WriteableObjectSignal<Integer> root = new WriteableObjectSignal<>(null, null);
        root.set(10);
        Assert.assertEquals(10, (int) root.get());

        root.set(null);
        Thread.sleep(15);
        Assert.assertNull(root.get());
    }
}