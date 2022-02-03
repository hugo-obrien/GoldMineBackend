package com.blackcubes.skarger.goldmine.backend;

import com.blackcubes.skarger.goldmine.backend.model.Booster;
import com.blackcubes.skarger.goldmine.backend.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.blackcubes.skarger.goldmine.backend.TestUtils.printHighlighted;

public class UserTests {

    @Test
    public void addBoosterTest() {
        User user = new User("some id");
        int boosterAmount = user.getBoosterAmount(Booster.DWARF);
        System.out.println("Dwarfves before: " + boosterAmount);
        user.addBooster(Booster.DWARF);
        boosterAmount = user.getBoosterAmount(Booster.DWARF);
        System.out.println("Dwarfves after: " + boosterAmount);
        Assertions.assertTrue(boosterAmount != 0);
    }

    @Test
    public void calculatePriceTest() {
        User user = new User("some id");
        final Booster shovel = Booster.SHOVEL;
        for (int i = 0; i < (Math.random() * 50); i++) {
            user.addBooster(shovel);
        }
        final int amount = user.getBoosterAmount(shovel);
        final long priceOfNext = user.calculateBoosterPrice(shovel);
        printHighlighted("Boosters: %s. Price of next: %s%n", amount, priceOfNext);
        Assertions.assertTrue(priceOfNext > shovel.priceOfFirst());
    }

    @Test
    public void peckTest() {
        User user = new User("some_id");
        final long initialGold = user.getGoldAmount();
        user.peck();
        final long afterFirstPeck = user.getGoldAmount();
        Assertions.assertEquals(initialGold + 1, afterFirstPeck);
        long pecks = Math.round(Math.random() * 1000);
        user.multiplePecks((int) pecks);
        final long afterMultiplePecks = user.getGoldAmount();
        Assertions.assertEquals(afterFirstPeck + pecks, afterMultiplePecks);
        printHighlighted("Initial gold: %s, after first peck: %s, after %s pecks: %s%n",
                initialGold, afterFirstPeck, pecks, afterMultiplePecks);
    }
}
