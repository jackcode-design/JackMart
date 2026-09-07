package com.jack.jackmart.dao;
import com.jack.jackmart.model.User;
import java.util.Optional;
public interface UserDAO { Optional<User> findByEmail(String email) throws Exception; long create(User user) throws Exception; }