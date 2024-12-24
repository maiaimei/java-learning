package org.example;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.Dao;
import org.example.dao.impl.UserDao;
import org.example.entity.User;
import org.junit.jupiter.api.Test;

@Slf4j
public class UserDaoTest {

  @Test
  public void test_findAll() {
    Map<Integer, User> users = new HashMap<>();
    users.put(1, new User("Julie"));
    users.put(2, new User("David"));
    Dao<User> userDao = new UserDao(users);
    userDao.findAll().forEach(user -> log.info("{}", user));
  }
}
