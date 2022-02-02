package com.blackcubes.skarger.goldmine.backend;

import com.blackcubes.skarger.goldmine.backend.services.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainLoop implements Runnable {

    private long lastTimeUpdated;

    @Autowired
    private UserService userService;

    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            //TODO при необходимости можно будет переделать на более редкий общий пересчет и принудительный персчет по
            // запросу (добавитб поле с временем последнего обновления)
            int delay = 1000; // one second
            if (System.currentTimeMillis() - lastTimeUpdated >= delay) {

                userService.tick();

                lastTimeUpdated = System.currentTimeMillis();
            } else {
                wait(lastTimeUpdated + delay - System.currentTimeMillis());
            }
        }
    }
}
