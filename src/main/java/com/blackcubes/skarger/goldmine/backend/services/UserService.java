package com.blackcubes.skarger.goldmine.backend.services;

import com.blackcubes.skarger.goldmine.backend.model.Booster;
import com.blackcubes.skarger.goldmine.backend.model.User;

import java.util.Map;

public interface UserService extends Runnable {

    void register(String userId);

    boolean isUserExists(String userId);

    User getUserById(String userId);

    long getGoldAmount(String userId);

    void peck(String userId);

    void peck(String userId, int numberOfPecks);

    void addBooster(String userId, Booster booster);

    void addBooster(String userId, Booster booster, int number);

    int getBoosterAmount(String userId, Booster booster);

    long calculateBoosterPrice(String userId, Booster booster);

    void tick();

    boolean buyBooster(String userId, Booster booster);

    Map<Booster, Integer> getUserBoosters(String userId);
}
