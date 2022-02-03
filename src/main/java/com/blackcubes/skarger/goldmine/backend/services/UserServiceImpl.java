package com.blackcubes.skarger.goldmine.backend.services;

import com.blackcubes.skarger.goldmine.backend.model.Booster;
import com.blackcubes.skarger.goldmine.backend.model.User;
import com.blackcubes.skarger.goldmine.backend.storage.UserDao;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserServiceImpl implements UserService {

    private final Map<String, User> users = new ConcurrentHashMap<>();

    @Autowired
    private UserDao userDao;

    @Value("${user.service.save.period:60000}")
    private int savePeriod;

    @PostConstruct
    private void initialize() {
        userDao.loadUsers().forEach(user -> users.put(user.getId(), user));
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
    public void addBooster(String userId, Booster booster, int number) {
        User user = users.get(userId);
        if (user != null) {
            user.addBooster(booster, number);
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
    public boolean buyBooster(String userId, Booster booster) {
        User user = users.get(userId);
        return user.buyBooster(booster);
    }

    @Override
    public Map<Booster, Integer> getUserBoosters(String userId) {
        final User user = users.get(userId);
        return user.getBoosters();
    }

    @SneakyThrows
    @Override
    public void run() {
        long lastUpdate = System.currentTimeMillis();
        while (true) {
            if (System.currentTimeMillis() - lastUpdate > savePeriod) {
                System.out.println("Update users call");
                userDao.updateUsers(users);
                lastUpdate = System.currentTimeMillis();
            } else {
                final long timeout = lastUpdate + savePeriod - System.currentTimeMillis();
                System.out.println("User save delayed for " + timeout);
                Thread.sleep(timeout);
            }
        }
    }
}
