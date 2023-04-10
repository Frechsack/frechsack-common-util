package frechsack.prod.util.concurrent.flow;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class AsyncOnDemandSubscriberTest {

    @Test
    public void onSubscribeSync() {
        try(SyncPublisher<Integer> publisher = new SyncPublisher<>()) {
            List<Thread> onSubscribe = Collections.synchronizedList(new ArrayList<>());
            AsyncOnDemandSubscriber<Integer> subscriber = new AsyncOnDemandSubscriber<>(new CompactSubscriber<>(){
                @Override
                public void onSubscribe(Flow.Subscription subscription) {
                    super.onSubscribe(subscription);
                    subscription.request(1);
                    onSubscribe.add(Thread.currentThread());
                }
            }, () -> false);
            publisher.subscribe(subscriber);
            publisher.submit(1);
            Assert.assertEquals(Thread.currentThread(), onSubscribe.get(0));
        }
    }

    @Test
    public void onSubscribeAsync() {
        try(SyncPublisher<Integer> publisher = new SyncPublisher<>()) {
            List<Thread> onSubscribe = Collections.synchronizedList(new ArrayList<>());
            AsyncOnDemandSubscriber<Integer> subscriber = new AsyncOnDemandSubscriber<>(new CompactSubscriber<>(){
                @Override
                public void onSubscribe(Flow.Subscription subscription) {
                    super.onSubscribe(subscription);
                    subscription.request(1);
                    onSubscribe.add(Thread.currentThread());
                }
            }, () -> true);
            publisher.subscribe(subscriber);
            publisher.submit(1);
            Thread.sleep(5);

            Assert.assertNotEquals(Thread.currentThread(), onSubscribe.get(0));
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void onNext() {
        try(SyncPublisher<Integer> publisher = new SyncPublisher<>()) {
            AtomicInteger elementsSubmitted = new AtomicInteger(0);
            List<Thread> onNext = Collections.synchronizedList(new ArrayList<>());
            AsyncOnDemandSubscriber<Integer> subscriber = new AsyncOnDemandSubscriber<>(new CompactSubscriber<>(Long.MAX_VALUE){
                @Override
                public void onNext(Integer item) {
                    onNext.add(Thread.currentThread());
                }
            }, () -> elementsSubmitted.get() % 2 == 0);
            publisher.subscribe(subscriber);

            Thread.sleep(5);
            for (int i = 0; i < 100; i++){
                publisher.submit(i);
                Thread.sleep(10);
                elementsSubmitted.incrementAndGet();

            }
            Thread.sleep(10);
            publisher.close();

            Assert.assertEquals(50,onNext.stream().filter(it -> it == Thread.currentThread()).count());

        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void onErrorSync() {
        try(SyncPublisher<Integer> publisher = new SyncPublisher<>()) {
            List<Thread> onError = Collections.synchronizedList(new ArrayList<>());
            AsyncOnDemandSubscriber<Integer> subscriber = new AsyncOnDemandSubscriber<>(new CompactSubscriber<>(200){
                @Override
                public void onError(Throwable throwable) {
                    onError.add(Thread.currentThread());
                }

                @Override
                public void onSubscribe(Flow.Subscription subscription) {
                    super.onSubscribe(subscription);
                    subscription.request(0);
                }
            }, () -> false);
            publisher.subscribe(subscriber);
            publisher.submit(1);
            Assert.assertEquals(Thread.currentThread(), onError.get(0));
        }
    }

    @Test
    public void onErrorAsync() {
        try(SyncPublisher<Integer> publisher = new SyncPublisher<>()) {
            List<Thread> onError = Collections.synchronizedList(new ArrayList<>());
            AsyncOnDemandSubscriber<Integer> subscriber = new AsyncOnDemandSubscriber<>(new CompactSubscriber<>(200){
                @Override
                public void onError(Throwable throwable) {
                    onError.add(Thread.currentThread());
                }

                @Override
                public void onSubscribe(Flow.Subscription subscription) {
                    super.onSubscribe(subscription);
                    subscription.request(0);
                }
            }, () -> true);
            publisher.subscribe(subscriber);
            publisher.submit(1);
            Thread.sleep(5);

            Assert.assertNotEquals(Thread.currentThread(), onError.get(0));
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void onCompleteAsync() {
        try(SyncPublisher<Integer> publisher = new SyncPublisher<>()) {
            List<Thread> onComplete = Collections.synchronizedList(new ArrayList<>());
            AsyncOnDemandSubscriber<Integer> subscriber = new AsyncOnDemandSubscriber<>(new CompactSubscriber<>(200){
                @Override
                public void onComplete() {
                    onComplete.add(Thread.currentThread());
                }
            }, () -> true);
            publisher.subscribe(subscriber);
            publisher.submit(1);
            Thread.sleep(15);
            publisher.close();
            Thread.sleep(15);
            Assert.assertNotEquals(Thread.currentThread(), onComplete.get(0));
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
    @Test
    public void onCompleteSync() {
        try(SyncPublisher<Integer> publisher = new SyncPublisher<>()) {
            List<Thread> onComplete = Collections.synchronizedList(new ArrayList<>());
            AsyncOnDemandSubscriber<Integer> subscriber = new AsyncOnDemandSubscriber<>(new CompactSubscriber<>(200){
                @Override
                public void onComplete() {
                    onComplete.add(Thread.currentThread());
                }
            }, () -> false);
            publisher.subscribe(subscriber);
            publisher.submit(1);
            publisher.close();
            Assert.assertEquals(Thread.currentThread(), onComplete.get(0));
        }
    }
}