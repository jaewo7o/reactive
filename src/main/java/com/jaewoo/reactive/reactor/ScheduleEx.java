package com.jaewoo.reactive.reactor;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ScheduleEx {
    public static void main(String[] args) {
        Publisher<Integer> publisher = sub -> {
            sub.onSubscribe(new Subscription() {
                @Override
                public void request(long n) {
                    sub.onNext(1);
                    sub.onNext(2);
                    sub.onNext(3);
                    sub.onNext(4);
                    sub.onNext(5);
                    sub.onComplete();
                }

                @Override
                public void cancel() {

                }
            });
        };

        Publisher<Integer> subOnPub = sub -> {
            ExecutorService es = Executors.newSingleThreadExecutor();
            es.execute(() -> publisher.subscribe(sub));
        };

        subOnPub.subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                log.debug("onSubscribe");
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                log.debug("onNext : {}", integer);
            }

            @Override
            public void onError(Throwable t) {
                log.debug("onError : {}", t);
            }

            @Override
            public void onComplete() {
                log.debug("onComplete");
            }
        });

        log.debug("EXIT");
    }
}
