package frechsack.prod.util.concurrent.flow;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class AsyncSubscriberTest {

    @Test
    public void onSubscribe() {
        try(SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>()) {
            AtomicInteger onSubscribe = new AtomicInteger(0);
            AsyncSubscriber<Integer> subscriber = new AsyncSubscriber<>(new CompactSubscriber<>(){
                @Override
                public void onSubscribe(Flow.Subscription subscription) {
                    super.onSubscribe(subscription);
                    subscription.request(1);
                    onSubscribe.incrementAndGet();
                }
            });
            publisher.subscribe(subscriber);
            publisher.submit(1);
            Thread.sleep(5);
            Assert.assertEquals(1, onSubscribe.get());

            publisher.submit(0);
            Thread.sleep(5);
            Assert.assertEquals(1, onSubscribe.get());

        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void onNext() {
        try(SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>()) {
            AtomicInteger onNext = new AtomicInteger(0);
            AsyncSubscriber<Integer> subscriber = new AsyncSubscriber<>(new CompactSubscriber<>(Long.MAX_VALUE){
                @Override
                public void onNext(Integer item) {
                    onNext.incrementAndGet();
                }
            });
            publisher.subscribe(subscriber);
            Thread.sleep(5);
            Assert.assertEquals(0, onNext.get());

            IntStream.range(0, 2000).forEach(publisher::submit);
            Thread.sleep(15);
            Assert.assertEquals(2000, onNext.get());

        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void onError() {
        try(SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>()) {
            AtomicInteger onError = new AtomicInteger(0);
            AsyncSubscriber<Integer> subscriber = new AsyncSubscriber<>(new CompactSubscriber<>(Long.MAX_VALUE){
                @Override
                public void onError(Throwable throwable) {
                    onError.incrementAndGet();
                }

                @Override
                public void onNext(Integer item) {
                    if (item == 100)
                        subscription.request(0);
                }
            });
            publisher.subscribe(subscriber);
            Thread.sleep(5);
            Assert.assertEquals(0, onError.get());

            IntStream.range(0, 2000).forEach(publisher::submit);
            Thread.sleep(15);
            Assert.assertEquals(1, onError.get());

        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void onComplete() {
        try(SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>()) {
            AtomicInteger onComplete = new AtomicInteger(0);
            AsyncSubscriber<Integer> subscriber = new AsyncSubscriber<>(new CompactSubscriber<>(Long.MAX_VALUE){
                @Override
                public void onComplete() {
                    onComplete.incrementAndGet();
                }
            });
            publisher.subscribe(subscriber);
            Thread.sleep(5);
            Assert.assertEquals(0, onComplete.get());

            IntStream.range(0, 2000).forEach(publisher::submit);
            Thread.sleep(15);
            Assert.assertEquals(0, onComplete.get());
            publisher.close();
            Thread.sleep(15);
            Assert.assertEquals(1, onComplete.get());

        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}