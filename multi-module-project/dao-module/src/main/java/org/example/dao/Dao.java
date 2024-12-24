package org.example.dao;

import java.util.List;

public interface Dao<T> {

  T findById(int id);

  List<T> findAll();

}