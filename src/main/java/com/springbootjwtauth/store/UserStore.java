package com.springbootjwtauth.store;

import com.springbootjwtauth.model.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserStore {

    private final Map<String, User> store = new HashMap<>();

    public boolean exists(String username) {
        return store.containsKey(username);
    }

    public void save(User user) {
        store.put(user.getUsername(), user);
    }

    public User findByUsername(String username) {
        return store.get(username);
    }

    public Map<String, User> getAll() {
        return store;
    }
}
