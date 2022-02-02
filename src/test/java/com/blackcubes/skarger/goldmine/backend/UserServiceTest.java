package com.blackcubes.skarger.goldmine.backend;

import com.blackcubes.skarger.goldmine.backend.services.UserService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

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
        //TODO
    }

    private String generateUser() {
        final String mockUserId = UUID.randomUUID().toString();
        System.out.println("Registering user " + mockUserId);
        userService.register(mockUserId);
        return mockUserId;
    }
}
