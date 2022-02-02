package com.blackcubes.skarger.goldmine.backend.storage;

import com.blackcubes.skarger.goldmine.backend.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service("UserDaoStub")
public class UserDaoStub implements UserDao {

    private final Map<String, User> users = new HashMap<>();

    @PostConstruct
    private void initialize() {
        generateUsers(5);
    }

    @Override
    public void register(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public User getUserFromDB(String id) {
        return users.get(id);
    }

    @Override
    public List<User> loadUsers() {
        return new ArrayList<>(users.values());
    }

    private void generateUsers(int number) {
        for (int i = 0; i < number; i++) {
            User user = generateUser();
            users.put(user.getId(), user);
        }
    }

    private User generateUser() {
        String userId = UUID.randomUUID().toString();
        return new User(userId);
    }
}
