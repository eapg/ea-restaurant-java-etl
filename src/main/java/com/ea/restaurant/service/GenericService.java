package com.ea.restaurant.service;

import java.util.List;
import java.util.Optional;

public interface GenericService<E, K> {

  E create(E entity);

  List<E> findAllEnabled();

  Optional<E> findEnabledById(K id);

  void deleteUserById(K id, E entity);

  E updateById(K id, E entity);
}
