package com.ea.restaurant.util;

import com.ea.restaurant.document.MongoOrderStatusHistory;
import java.util.List;

public class GrpcMongoOrderStatusHistoryUtil {

  public static List<String> getMongoOrderStatusUuids(
      List<MongoOrderStatusHistory> mongoOrderStatusHistoryList) {
    return mongoOrderStatusHistoryList.stream()
        .map(mongoOrderStatusHistory -> mongoOrderStatusHistory.getId().toString())
        .toList();
  }
}
