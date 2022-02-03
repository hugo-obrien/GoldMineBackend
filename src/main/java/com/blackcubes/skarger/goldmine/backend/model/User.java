package com.blackcubes.skarger.goldmine.backend.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Data
public class User {

    private String id;
    private long goldAmount;
    private final Map<Booster, Integer> boosters = new HashMap<>();

    public User(String id) {
        this.id = id;
    }

    public void addBooster(Booster booster) {
        addBooster(booster, 1);
    }

    public void addBooster(Booster booster, int number) {
        boosters.compute(booster, (k, v) -> (v == null) ? number : v + number);
    }

    public int getBoosterAmount(Booster booster) {
        Integer amount = boosters.get(booster);
        return amount != null ? amount : 0;
    }

    public long calculateBoosterPrice(Booster booster) {
        Integer amount = boosters.get(booster);
        if (amount == null) {
            return booster.priceOfFirst();
        } else {
            long price = booster.priceOfFirst();
            for (int i = 1; i <= amount; i++) {
                price = Math.round(price * booster.priceIncrease());
            }
            return price;
        }
    }

    /**
     * Increase gold due to users activity
     */
    public void peck() {
        goldAmount++;
    }

    /**
     * Increase gold due to users activity
     *
     * @param number - number of users pecks
     */
    public void multiplePecks(int number) {
        goldAmount += number;
    }


    /**
     * Increase gold due to user boosters
     */
    public void tick() {
        AtomicLong additionalGold = new AtomicLong();
        boosters.forEach((booster, integer) -> {
            additionalGold.addAndGet(booster.boostAmount() * integer);
        });
        goldAmount += additionalGold.get();
    }

    public boolean buyBooster(Booster booster) {
        long boosterPrice = calculateBoosterPrice(booster);
        if (boosterPrice > goldAmount) {
            return false;
        }
        goldAmount -= boosterPrice;
        addBooster(booster);
        return true;
    }
}
