package com.ea.restaurant.service.impl;

import com.ea.restaurant.constants.OrderStatus;
import com.ea.restaurant.repository.OrderStatusHistoryRepository;
import com.ea.restaurant.service.OrderStatusHistoryService;
import com.ea.restaurant.test.fixture.OrderStatusHistoryFixture;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class OrderStatusHistoryServiceImplTest {
  private OrderStatusHistoryService orderStatusHistoryService;
  private OrderStatusHistoryRepository orderStatusHistoryRepository;
  private AutoCloseable mockedAnnotations;

  @BeforeEach
  public void setUp() {
    orderStatusHistoryRepository = Mockito.mock(OrderStatusHistoryRepository.class);
    orderStatusHistoryService = new OrderStatusHistoryServiceImpl(orderStatusHistoryRepository);
    this.mockedAnnotations = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  public void afterEach() throws Exception {
    this.mockedAnnotations.close();
  }

  @Test
  public void findLastOrderStatusHistoriesByOrderIds() {
    var orderStatus1 = OrderStatusHistoryFixture.buildOrderStatusHistory(1L);
    var orderStatus2 = OrderStatusHistoryFixture.buildOrderStatusHistory(2L);
    orderStatus2.setFromStatus(OrderStatus.IN_PROCESS);
    var orderStatusHistories = List.of(orderStatus1, orderStatus2);

    Mockito.when(
            this.orderStatusHistoryRepository.findLastOrderStatusHistoriesByOrderIds(
                Mockito.eq(List.of(1L))))
        .thenReturn(orderStatusHistories);
    var orderStatusHistoriesReturned =
        this.orderStatusHistoryService.findLastOrderStatusHistoriesByOrderIds(List.of(1L));
    Mockito.verify(this.orderStatusHistoryRepository, Mockito.times(1))
        .findLastOrderStatusHistoriesByOrderIds(Mockito.eq(List.of(1L)));
    Assertions.assertEquals(orderStatusHistories, orderStatusHistoriesReturned);
  }

  @Test
  public void whenInsertNewOrUpdate_ShouldBulkInsertOrderStatusHistories() {
    var orderStatus1 = OrderStatusHistoryFixture.buildOrderStatusHistory(1L);
    var orderStatus2 = OrderStatusHistoryFixture.buildOrderStatusHistory(2L);
    var orderStatusHistories = List.of(orderStatus1, orderStatus2);

    this.orderStatusHistoryService.insertNewOrUpdatedBatchOrderStatusHistories(
        orderStatusHistories);

    Mockito.verify(orderStatusHistoryRepository, Mockito.times(1))
        .saveAll(Mockito.eq(orderStatusHistories));
  }
}
