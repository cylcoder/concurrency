package com.example.stock.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedissonLockStockFacadeTest {

  @Autowired
  private RedissonLockStockFacade redissonLockStockFacade;

  @Autowired
  private StockRepository stockRepository;

  @BeforeEach
  void setUp() {
    stockRepository.save(new Stock(1L, 100L));
  }

  @Test
  void concurrentDecreaseStock() throws InterruptedException {
    int threadCount = 100;
    ExecutorService executorService = Executors.newFixedThreadPool(32);
    CountDownLatch countDownLatch = new CountDownLatch(threadCount);

    for (int i = 0; i < threadCount; i++) {
      executorService.submit(() -> {
        try {
          redissonLockStockFacade.decreaseStock(1L, 1L);
        } finally {
          countDownLatch.countDown();
        }
      });
    }

    countDownLatch.await();

    Long remainingQuantity = stockRepository.findById(1L).orElseThrow().getQuantity();
    assertThat(remainingQuantity).isZero();
  }

}