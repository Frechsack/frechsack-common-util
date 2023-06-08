package frechsack.prod.util.concurrent.flow;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class AsyncPublisherTest {

    @Test
    public void subscribeWithDemandAtArrivalBlocking() throws InterruptedException {
        AsyncPublisher<Integer> publisher = new AsyncPublisher<>(null, 15);
        AtomicInteger onSubscribeCall = new AtomicInteger();
        AtomicInteger onNextCall = new AtomicInteger();
        AtomicInteger onErrorCall = new AtomicInteger();
        AtomicInteger onCompleteCall = new AtomicInteger();
        publisher.subscribe(new Flow.Subscriber<>() {
            Flow.Subscription subscription;
            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                this.subscription = subscription;
                onSubscribeCall.incrementAndGet();
                subscription.request(1);
            }

            @Override
            public void onNext(Integer item) {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                onNextCall.incrementAndGet();
                subscription.request(1);
            }

            @Override
            public void onError(Throwable throwable) {
                onErrorCall.incrementAndGet();
            }

            @Override
            public void onComplete() {
                onCompleteCall.incrementAndGet();
            }
        });
        IntStream.range(0, 10).forEach(publisher::submit);
        Thread.sleep(3000);
        publisher.close();
        Thread.sleep(10);
        Assert.assertEquals(1, onSubscribeCall.get());
        Assert.assertEquals(10, onNextCall.get());
        Assert.assertEquals(0, onErrorCall.get());
        Assert.assertEquals(1, onCompleteCall.get());
        Assert.assertFalse(publisher.hasSubscribers());
    }

    @Test
    public void subscribeWithException() throws InterruptedException {
        AsyncPublisher<Integer> publisher = new AsyncPublisher<>(null,15);
        AtomicInteger onSubscribeCall = new AtomicInteger();
        AtomicInteger onNextCall = new AtomicInteger();
        AtomicInteger onErrorCall = new AtomicInteger();
        AtomicInteger onCompleteCall = new AtomicInteger();
        publisher.subscribe(new Flow.Subscriber<>() {
            Flow.Subscription subscription;
            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                this.subscription = subscription;
                onSubscribeCall.incrementAndGet();
                subscription.request(1);
            }

            @Override
            public void onNext(Integer item) {
                onNextCall.incrementAndGet();
                if (item == 5)
                    subscription.request(0);
                else
                    subscription.request(1);
            }

            @Override
            public void onError(Throwable throwable) {
                onErrorCall.incrementAndGet();
            }

            @Override
            public void onComplete() {
                onCompleteCall.incrementAndGet();
            }
        });
        IntStream.range(0, 10).forEach(publisher::submit);

        Thread.sleep(15);
        publisher.close();
        Thread.sleep(10);

        Assert.assertEquals(1, onSubscribeCall.get());
        Assert.assertEquals(6, onNextCall.get());
        Assert.assertEquals(1, onErrorCall.get());
        Assert.assertEquals(0, onCompleteCall.get());
    }

    @Test
    public void subscribeWithDemandAtArrival() throws InterruptedException {
        AsyncPublisher<Integer> publisher = new AsyncPublisher<>(null, 15);
        AtomicInteger onSubscribeCall = new AtomicInteger();
        AtomicInteger onNextCall = new AtomicInteger();
        AtomicInteger onErrorCall = new AtomicInteger();
        AtomicInteger onCompleteCall = new AtomicInteger();
        publisher.subscribe(new Flow.Subscriber<>() {
            Flow.Subscription subscription;
            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                this.subscription = subscription;
                onSubscribeCall.incrementAndGet();
                subscription.request(1);
            }

            @Override
            public void onNext(Integer item) {
                onNextCall.incrementAndGet();
                subscription.request(1);
            }

            @Override
            public void onError(Throwable throwable) {
                onErrorCall.incrementAndGet();
            }

            @Override
            public void onComplete() {
                onCompleteCall.incrementAndGet();
            }
        });
        IntStream.range(0, 10).forEach(publisher::submit);

        Thread.sleep(15);
        publisher.close();
        Thread.sleep(10);

        Assert.assertEquals(1, onSubscribeCall.get());
        Assert.assertEquals(10, onNextCall.get());
        Assert.assertEquals(0, onErrorCall.get());
        Assert.assertEquals(1, onCompleteCall.get());
    }

    @Test
    public void subscribeWithDemandAtOnce() throws InterruptedException {
        AsyncPublisher<Integer> publisher = new AsyncPublisher<>(null, 15);
        AtomicInteger onSubscribeCall = new AtomicInteger();
        AtomicInteger onNextCall = new AtomicInteger();
        AtomicInteger onErrorCall = new AtomicInteger();
        AtomicInteger onCompleteCall = new AtomicInteger();
        publisher.subscribe(new Flow.Subscriber<>() {
            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                onSubscribeCall.incrementAndGet();
                subscription.request(10);
            }

            @Override
            public void onNext(Integer item) {
                onNextCall.incrementAndGet();
            }

            @Override
            public void onError(Throwable throwable) {
                onErrorCall.incrementAndGet();
            }

            @Override
            public void onComplete() {
                onCompleteCall.incrementAndGet();
            }
        });
        IntStream.range(0, 10).forEach(publisher::submit);

        Thread.sleep(15);
        publisher.close();
        Thread.sleep(10);

        Assert.assertEquals(1, onSubscribeCall.get());
        Assert.assertEquals(10, onNextCall.get());
        Assert.assertEquals(0, onErrorCall.get());
        Assert.assertEquals(1, onCompleteCall.get());
    }

    @Test
    public void subscribeWithoutDemand() throws InterruptedException {
        AsyncPublisher<Integer> publisher = new AsyncPublisher<>(null, 15);
        AtomicInteger onSubscribeCall = new AtomicInteger();
        AtomicInteger onNextCall = new AtomicInteger();
        AtomicInteger onErrorCall = new AtomicInteger();
        AtomicInteger onCompleteCall = new AtomicInteger();
        publisher.subscribe(new Flow.Subscriber<>() {
            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                onSubscribeCall.incrementAndGet();
            }

            @Override
            public void onNext(Integer item) {
                onNextCall.incrementAndGet();
            }

            @Override
            public void onError(Throwable throwable) {
                onErrorCall.incrementAndGet();
            }

            @Override
            public void onComplete() {
                onCompleteCall.incrementAndGet();
            }
        });
        IntStream.range(0, 10).forEach(publisher::submit);

        Thread.sleep(15);
        publisher.close();
        Thread.sleep(10);

        Assert.assertEquals(1, onSubscribeCall.get());
        Assert.assertEquals(0, onNextCall.get());
        Assert.assertEquals(0, onErrorCall.get());
        Assert.assertEquals(1, onCompleteCall.get());
    }
}