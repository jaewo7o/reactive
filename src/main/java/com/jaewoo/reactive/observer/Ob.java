package com.jaewoo.reactive.observer;

import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("deprecation")
public class Ob {
//
//    public static void main(String[] args) {
//        Iterable<Integer> iter = () ->
//            new Iterator<Integer>() {
//                int i = 0;
//                final static int MAX = 10;
//
//                public boolean hasNext() {
//                    return i < MAX;
//                }
//
//                public Integer next() {
//                    return ++i;
//                }
//        };
//
//        for (Integer i : iter) {
//            System.out.println(i);
//        }
//    }

    static class IntObservable extends Observable implements Runnable {
        @Override
        public void run() {
            for (int i = 1; i <= 10; i++) {
                setChanged();
                notifyObservers(i);
            }
        }
    };
    public static void main(String[] args) {
        Observer ob1 = new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                System.out.println(String.format("o#1 : %s", arg));
            }
        };

        IntObservable io = new IntObservable();
        io.addObserver(ob1);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(io);

        System.out.println("========================================>");

        executorService.shutdown();
    }
}
