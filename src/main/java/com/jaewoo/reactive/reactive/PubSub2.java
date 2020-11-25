package com.jaewoo.reactive.reactive;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Reactive Stream : Operators
 * Publisher -> Data1 -> mapPublisher -> Date2 -> Subscriber
 */
@Slf4j
public class PubSub2 {

    public static void main(String[] args) {
        Publisher<Integer> publisher = getIterablePublisher(Stream.iterate(1, x -> x + 1).limit(10).collect(Collectors.toList()));

        Publisher<String> mapPublisher = mapPublisher(publisher, (Function<Integer, String>) x -> String.format("[ %d ]", x));
        mapPublisher.subscribe(getLogSubscriber());

        // Publisher<Integer> sumPublisher = sumPublisher(publisher);
        // sumPublisher.subscribe(getLogSubscriber());

        // Publisher<Integer> reducePublisher = reducePublisher(publisher, 0, (a, b) -> a + b);
        // reducePublisher.subscribe(getLogSubscriber());
    }

    private static Publisher<Integer> reducePublisher(Publisher<Integer> publisher, int init, BiFunction<Integer, Integer, Integer> function) {
        return new Publisher<Integer>() {
            @Override
            public void subscribe(Subscriber<? super Integer> subscriber) {
                publisher.subscribe(new DelegateSubscriber<Integer, Integer>(subscriber) {
                    int result = init;

                    @Override
                    public void onNext(Integer integer) {
                        result = function.apply(result, integer);
                    }

                    @Override
                    public void onComplete() {
                        subscriber.onNext(result);
                        subscriber.onComplete();
                    }
                });
            }
        };
    }

    private static Publisher<Integer> sumPublisher(Publisher<Integer> publisher) {
        return new Publisher<Integer>() {
            @Override
            public void subscribe(Subscriber<? super Integer> subscriber) {
                publisher.subscribe(new DelegateSubscriber<Integer, Integer>(subscriber) {
                    int sum = 0;

                    @Override
                    public void onNext(Integer integer) {
                        sum += integer;
                    }

                    @Override
                    public void onComplete() {
                        subscriber.onNext(sum);
                        subscriber.onComplete();
                    }
                });
            }
        };
    }

    private static <T, R> Publisher<R> mapPublisher(Publisher<T> publisher, Function<T, R> f) {
        return new Publisher<R>() {
            @Override
            public void subscribe(Subscriber<? super R> subscriber) {
                publisher.subscribe(new DelegateSubscriber<T, R>(subscriber) {
                    @Override
                    public void onNext(T value) {
                        subscriber.onNext(f.apply(value));
                    }
                });
            }
        };
    }

    private static <T> Subscriber<T> getLogSubscriber() {
        return new Subscriber<T>() {
            @Override
            public void onSubscribe(Subscription s) {
                log.debug("onSubscribe");
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(T integer) {
                log.debug("onNext:{}", integer);
            }

            @Override
            public void onError(Throwable t) {
                log.debug("onError:{}", t);
            }

            @Override
            public void onComplete() {
                log.debug("onComplete");
            }
        };
    }

    private static Publisher<Integer> getIterablePublisher(Iterable<Integer> inputIterable) {

        return new Publisher<Integer>() {
            Iterable<Integer> iterable = inputIterable;

            @Override
            public void subscribe(Subscriber<? super Integer> subscriber) {
                subscriber.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
                        try {
                            iterable.forEach(s -> subscriber.onNext(s));
                            subscriber.onComplete();
                        } catch (Throwable t) {
                            subscriber.onError(t);
                        }
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        };
    }
}
