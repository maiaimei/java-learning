package org.example.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.example.dao.Dao;
import org.example.entity.User;

public class UserDao implements Dao<User> {

  private final Map<Integer, User> users;

  public UserDao(Map<Integer, User> users) {
    this.users = users;
  }

  @Override
  public User findById(int id) {
    return users.get(id);
  }

  @Override
  public List<User> findAll() {
    return new ArrayList<>(users.values());
  }

}