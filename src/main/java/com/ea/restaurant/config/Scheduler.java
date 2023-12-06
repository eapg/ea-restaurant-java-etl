package com.ea.restaurant.config;

import com.ea.restaurant.etl.impl.MongoToPostgresqlOrderStatusHistoryEtlService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class Scheduler {

  private final MongoToPostgresqlOrderStatusHistoryEtlService
      mongoToPostgresqlOrderStatusHistoryEtlService;

  public Scheduler(
      MongoToPostgresqlOrderStatusHistoryEtlService mongoToPostgresqlOrderStatusHistoryEtlService) {
    this.mongoToPostgresqlOrderStatusHistoryEtlService =
        mongoToPostgresqlOrderStatusHistoryEtlService;
  }

  @Scheduled(fixedRate = 5000)
  public void runMongoToPostgresqlEtl() {

    var extractedData = this.mongoToPostgresqlOrderStatusHistoryEtlService.extract();
    if (!extractedData.isEmpty()) {
      var transformedData =
          this.mongoToPostgresqlOrderStatusHistoryEtlService.transform(extractedData);
      this.mongoToPostgresqlOrderStatusHistoryEtlService.load(transformedData);
    }
  }
}
