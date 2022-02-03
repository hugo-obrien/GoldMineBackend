package com.blackcubes.skarger.goldmine.backend;

import com.blackcubes.skarger.goldmine.backend.services.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class MainLoop implements Runnable {

    private long lastTimeUpdated;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final Map<String, Future> services = new ConcurrentHashMap<>();

    @Autowired
    private UserService userService;

    @SneakyThrows
    @Override
    public void run() {

        services.put("UserService", executorService.submit(new Thread(userService, "UserService")));

        while (true) {
            //TODO при необходимости можно будет переделать на более редкий общий пересчет и принудительный персчет по
            // запросу (добавить поле с временем последнего обновления)
            int delay = 1000; // one second
            if (System.currentTimeMillis() - lastTimeUpdated >= delay) {

                userService.tick();

                lastTimeUpdated = System.currentTimeMillis();
            } else {
                final long timeout = lastTimeUpdated + delay - System.currentTimeMillis();
                Thread.sleep(timeout);
            }
        }
    }
}
