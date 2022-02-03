package com.blackcubes.skarger.goldmine.backend.storage;

import com.blackcubes.skarger.goldmine.backend.model.User;

import java.util.List;
import java.util.Map;

public interface UserDao {

    void register(User user);

    User getUserFromDB(String id);

    List<User> loadUsers();

    void updateUsers(Map<String, User> users);
}

