package com.blackcubes.skarger.goldmine.backend.storage;

import com.blackcubes.skarger.goldmine.backend.model.User;

import java.util.List;

public interface UserDao {

    void register(User user);

    User getUserFromDB(String id);

    List<User> loadUsers();
}

