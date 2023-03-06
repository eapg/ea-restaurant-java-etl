package com.ea.restaurant.repository;

import com.ea.restaurant.entities.OrderStatusHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, Long> {
  @Query(
      value =
          """
                    WITH latest_order_status_histories_cte AS (
                              SELECT osh.order_id, max(osh.from_time) AS max_from_time
                                FROM order_status_histories osh
                               WHERE osh.order_id IN :order_ids AND osh.entity_status = 'ACTIVE'
                               GROUP BY osh.order_id
                            )
                                SELECT osh.*
                                  FROM order_status_histories AS osh
                            INNER JOIN latest_order_status_histories_cte loshcte
                                    ON loshcte.order_id = osh.order_id AND loshcte.max_from_time = osh.from_time
                            """,
      nativeQuery = true)
  List<OrderStatusHistory> findLastOrderStatusHistoriesByOrderIds(
      @Param("order_ids") List<Long> orderIds);
}
