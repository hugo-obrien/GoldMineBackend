package com.blackcubes.skarger.goldmine.backend;

import com.blackcubes.skarger.goldmine.backend.model.Booster;
import com.blackcubes.skarger.goldmine.backend.services.UserService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import static com.blackcubes.skarger.goldmine.backend.TestUtils.printHighlighted;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {com.blackcubes.skarger.goldmine.backend.CommonContextConfiguration.class})
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void healthCheck() {
        Assertions.assertNotNull(userService);
    }

    @Test
    public void registerTest() {
        final String mockUserId = generateUser();
        Assertions.assertTrue(userService.isUserExists(mockUserId));
        System.out.println("Users " + mockUserId + " created");
    }

    @Test
    public void getGoldTest() {
        final String mockUserId = generateUser();

        long goldAmount = userService.getGoldAmount(mockUserId);
        System.out.println("Gold " + goldAmount);
        Assertions.assertEquals(goldAmount, 0L);
    }

    @Test
    public void pecksTest() {
        final String mockUserId = generateUser();

        userService.peck(mockUserId);
        long goldAmount = userService.getGoldAmount(mockUserId);
        System.out.println("Gold " + goldAmount);
        Assertions.assertEquals(goldAmount, 1L);

        long pecksNumber = Math.round(Math.random() * 10);
        System.out.println("Pecks number " + pecksNumber);
        userService.peck(mockUserId, (int) pecksNumber);
        goldAmount = userService.getGoldAmount(mockUserId);
        System.out.println("Gold " + goldAmount);
        Assertions.assertEquals(goldAmount, 1L + pecksNumber);
    }

    @Test
    public void addBoosterTest() {
        final Booster booster = Booster.PICK;
        final String mockUserId = generateUser();

        long boostersCounter = Math.round(Math.random() * 10);
        userService.addBooster(mockUserId, booster, (int) boostersCounter);
        Assertions.assertEquals(boostersCounter, userService.getBoosterAmount(mockUserId, booster));
    }

    @Test
    public void tickBoosterTest() {
        final Booster booster = Booster.PERFORATOR;
        final String mockUserId = generateUser();

        long boostersCounter = Math.round(Math.random() * 10);
        userService.addBooster(mockUserId, booster, (int) boostersCounter);

        final long goldBefore = userService.getGoldAmount(mockUserId);
        long tickCounter = Math.round(Math.random() * 10);
        for (int i = 0; i < tickCounter; i++) {
            userService.tick();
        }

        final long goldAfter = userService.getGoldAmount(mockUserId);
        final long expected = goldBefore + booster.boostAmount() * boostersCounter * tickCounter;
        printHighlighted("Gold before: %s, boosters: %s, boostAmount: %s, ticks: %s, expected: %s, gold after: %s%n",
                goldBefore, boostersCounter, booster.boostAmount(), tickCounter, expected, goldAfter);
        Assertions.assertEquals(expected, goldAfter);
    }

    @Test
    public void tickComplexBoosterTest() {

        String mockUserId = generateUserWithRandomBoosters();
        final Map<Booster, Integer> boostersAmount = userService.getUserBoosters(mockUserId);

        final long goldBefore = userService.getGoldAmount(mockUserId);
        long tickCounter = Math.round(Math.random() * 10);
        for (int i = 0; i < tickCounter; i++) {
            userService.tick();
        }
        final long goldAfter = userService.getGoldAmount(mockUserId);

        AtomicLong expected = new AtomicLong(goldBefore);
        for (int i = 0; i < tickCounter; i++) {
            boostersAmount.forEach((booster, amount) -> {
                expected.addAndGet(booster.boostAmount() * amount);
            });
        }
        printHighlighted("Expected: %s, fact: %s%n", expected.get(), goldAfter);
        Assertions.assertEquals(expected.get(), goldAfter);
    }

    @Test
    public void buyBoosterTest() {
        final String mockUserId = generateUserWithRandomBoosters();
        boolean enoughGoldChecked = false;
        boolean notEnoughGoldChecked = false;
        while (!enoughGoldChecked || !notEnoughGoldChecked) {
            Booster booster = TestUtils.getRandomBooster();
            final long goldAmountBefore = userService.getGoldAmount(mockUserId);
            final long price = userService.calculateBoosterPrice(mockUserId, booster);
            final int boosterAmountBefore = userService.getBoosterAmount(mockUserId, booster);
            final boolean isBoughtSuccess = userService.buyBooster(mockUserId, booster);

            if (goldAmountBefore < price && !notEnoughGoldChecked) {
                Assertions.assertFalse(isBoughtSuccess);
                Assertions.assertEquals(boosterAmountBefore, userService.getBoosterAmount(mockUserId, booster));
                Assertions.assertEquals(goldAmountBefore, userService.getGoldAmount(mockUserId));
                notEnoughGoldChecked = true;
            }

            if (goldAmountBefore >= price && !enoughGoldChecked) {
                Assertions.assertTrue(isBoughtSuccess);
                Assertions.assertEquals(boosterAmountBefore + 1, userService.getBoosterAmount(mockUserId, booster));
                Assertions.assertEquals(goldAmountBefore - price, userService.getGoldAmount(mockUserId));
                enoughGoldChecked = true;
            }

            userService.peck(mockUserId);
            userService.tick();
        }
    }

    private String generateUserWithRandomBoosters() {
        final String mockUserId = generateUser();

        for (Booster booster : Booster.values()) {
            final long boosterCounter = Math.round(Math.random() * 5);
            userService.addBooster(mockUserId, booster, (int) boosterCounter);
        }

        return mockUserId;
    }

    private String generateUser() {
        final String mockUserId = UUID.randomUUID().toString();
        System.out.println("Registering user " + mockUserId);
        userService.register(mockUserId);
        return mockUserId;
    }
}
