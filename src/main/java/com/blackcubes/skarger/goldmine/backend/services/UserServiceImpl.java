package com.blackcubes.skarger.goldmine.backend.services;

import com.blackcubes.skarger.goldmine.backend.model.Booster;
import com.blackcubes.skarger.goldmine.backend.model.User;
import com.blackcubes.skarger.goldmine.backend.storage.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserServiceImpl implements UserService, Runnable {

    Map<String, User> users = new ConcurrentHashMap<>();

    @Autowired
    private UserDao userDao;

    @PostConstruct
    private void initialize() {
        userDao.loadUsers().forEach(user -> users.put(user.getId(), user));
        run();
    }

    @Override
    public void register(String userId) {
        User user = new User(userId);
        userDao.register(user);
        users.put(user.getId(), user);
    }

    @Override
    public boolean isUserExists(String userId) {
        return getUserById(userId) != null;
    }

    @Override
    public User getUserById(String userId) {
        return users.computeIfAbsent(userId, (id -> userDao.getUserFromDB(id)));
    }

    @Override
    public long getGoldAmount(String userId) {
        return users.get(userId).getGoldAmount();
    }

    @Override
    public void peck(String userId) {
        users.get(userId).peck();
    }

    @Override
    public void peck(String userId, int numberOfPecks) {
        users.get(userId).multiplePecks(numberOfPecks);
    }

    @Override
    public void addBooster(String userId, Booster booster) {
        User user = users.get(userId);
        if (user != null) {
            user.addBooster(booster);
        }
    }

    @Override
    public int getBoosterAmount(String userId, Booster booster) {
        return users.get(userId).getBoosterAmount(booster);
    }

    @Override
    public long calculateBoosterPrice(String userId, Booster booster) {
        return users.get(userId).calculateBoosterPrice(booster);
    }

    @Override
    public void tick() {
        users.forEach((k, v) -> v.tick());
    }

    @Override
    public void run() {
        // TODO периодическое сохранение данных
        // TODO периодическая очистка users от пустых данных, которые могут генериться при вызове getUserById или isUserExists
    }
}
