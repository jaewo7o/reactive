package com.jaewoo.reactive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;


@SpringBootApplication
@Slf4j
@EnableAsync
public class ReactiveApplication {

//    @RestController
//    public static class Controller {
//        @RequestMapping("/hello")
//        public Publisher<String> hello(String name) {
//            return new Publisher<String>() {
//                @Override
//                public void subscribe(Subscriber<? super String> s) {
//                    s.onSubscribe(new Subscription() {
//                        @Override
//                        public void request(long n) {
//                            s.onNext("Hello -++ = " + name);
//                            s.onComplete();
//                        }
//
//                        @Override
//                        public void cancel() {
//
//                        }
//                    });
//                }
//            };
//        }
//    }

    // gradle dependency 변경 필요 Spring WEB MVC
    @RestController
    public static class MyController {
        @GetMapping("/callable")
        public Callable<String> async() throws InterruptedException {
            log.info("callable");
            return new Callable<String>() {
                @Override
                public String call() throws Exception {
                    log.info("async");
                    Thread.sleep(2000);
                    return "hello";
                }
            };
        }
    }

    @Service
    public static class MyService {
        public String async() {
            return "";
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(ReactiveApplication.class, args);
    }

}
