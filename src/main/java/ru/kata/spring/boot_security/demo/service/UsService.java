package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.Model.Role;
import ru.kata.spring.boot_security.demo.Model.User;

import java.util.Optional;

public interface UsService {
    void save(User user);
    Optional<User> findUserByHisName(String str);
    Optional<User> findByEmail(String email);
}
