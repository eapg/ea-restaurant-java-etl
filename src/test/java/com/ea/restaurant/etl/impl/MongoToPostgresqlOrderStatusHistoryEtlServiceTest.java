package com.ea.restaurant.etl.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

class MongoToPostgresqlOrderStatusHistoryEtlServiceTest {

  private AutoCloseable mockedAnnotations;

  @BeforeEach
  void setUp() {
    this.mockedAnnotations = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  void afterEach() throws Exception {
    this.mockedAnnotations.close();
  }

  @Test
  void extract() {}

  @Test
  void transform() {}

  @Test
  void load() {}
}
