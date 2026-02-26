package com.example.agent.service;

import com.example.agent.domain.User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    private final Map<String, User> users = new ConcurrentHashMap<>();

    public User create(String name, String email) {
        String id = UUID.randomUUID().toString();
        User user = new User(id, name, email);
        users.put(id, user);
        return user;
    }

    public Optional<User> getById(String id) {
        return Optional.ofNullable(users.get(id));
    }
}

