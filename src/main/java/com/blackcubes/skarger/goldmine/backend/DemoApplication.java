package com.blackcubes.skarger.goldmine.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    static MainLoop loop;

    @Autowired
    public void beginLoop(MainLoop loop) {
        DemoApplication.loop = loop;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        synchronized (loop) {
            loop.run();
        }
    }

}
