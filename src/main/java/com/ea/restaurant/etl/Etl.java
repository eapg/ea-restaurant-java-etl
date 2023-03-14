package com.ea.restaurant.etl;

import java.util.List;

public interface Etl<S, T> {
  List<S> extract();

  List<T> transform(List<S> extractedData);

  void load(List<T> transformedData);
}
