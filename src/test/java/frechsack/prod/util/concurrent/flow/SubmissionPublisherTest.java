package frechsack.prod.util.concurrent.flow;

import org.junit.Test;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class SubmissionPublisherTest {

    @Test
    public void nullItems(){
        try (SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>()){

            Flow.Subscriber<Integer> subscriber = new CompactSubscriber<>(Long.MAX_VALUE){
                @Override
                public void onNext(Integer item) {
                    System.out.println("OnNext: " + item);
                }
            };
            publisher.subscribe(subscriber);
            publisher.submit(0);
            publisher.submit(null);

            Thread.sleep(5);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
