package com.jaewoo.reactive.reactive;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class DelegateSubscriber<T, R> implements Subscriber<T> {
    Subscriber subscriber;

    public DelegateSubscriber(Subscriber<? super R> sub) {
        this.subscriber = sub;
    }

    @Override
    public void onSubscribe(Subscription s) {
        subscriber.onSubscribe(s);
    }

    @Override
    public void onNext(T value) {
        subscriber.onNext(value);
    }

    @Override
    public void onError(Throwable t) {
        subscriber.onError(t);
    }

    @Override
    public void onComplete() {
        subscriber.onComplete();
    }
}
