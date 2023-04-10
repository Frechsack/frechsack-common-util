package frechsack.prod.util.concurrent.flow;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SyncPublisherTest {

    @Test
    public void subscribe(){
        try (SyncPublisher<Integer> pub = new SyncPublisher<>()) {

            AtomicReference<Flow.Subscription> subscription = new AtomicReference<>();
            AtomicInteger onError = new AtomicInteger(0);
            AtomicInteger onComplete = new AtomicInteger(0);
            AtomicInteger onNext = new AtomicInteger(0);

            Flow.Subscriber<Integer> subscriber = new Flow.Subscriber<>() {
                @Override
                public void onSubscribe(Flow.Subscription sub) {
                    subscription.set(sub);
                    sub.request(100);
                }

                @Override
                public void onNext(Integer item) {
                    Assert.assertTrue(0 <= item && item < 200);
                    onNext.incrementAndGet();
                }

                @Override
                public void onError(Throwable throwable) {
                    onError.incrementAndGet();
                }

                @Override
                public void onComplete() {
                    onComplete.incrementAndGet();
                }
            };

            pub.subscribe(subscriber);
            for (int i = 0; i < 200; i++)
                pub.submit(i);

            Assert.assertEquals(100, onNext.get());
            Assert.assertEquals(0, onComplete.get());
            Assert.assertEquals(0, onError.get());

            subscription.get().request(100);
            Assert.assertEquals(200, onNext.get());
            Assert.assertEquals(0, onComplete.get());
            Assert.assertEquals(0, onError.get());

            subscription.get().cancel();
            Assert.assertEquals(1, onComplete.get());



        }
    }
}