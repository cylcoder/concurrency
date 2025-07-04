package com.example.stock.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.stock.domain.Stock;
import com.example.stock.facade.OptimisticLockStockFacade;
import com.example.stock.repository.StockRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StockServiceTest {

  @Autowired
  private StockService stockService;

  @Autowired
  private PessimisticLockStockService pessimisticLockStockService;

  @Autowired
  private OptimisticLockStockFacade optimisticLockStockFacade;

  @Autowired
  private StockRepository stockRepository;

  @BeforeEach
  void setUp() {
    stockRepository.save(new Stock(1L, 100L));
  }

  @Test
  void decreaseStock() {
    stockService.decreaseStock(1L, 1L);
    Long remainingQuantity = stockRepository.findById(1L).orElseThrow().getQuantity();
    assertThat(remainingQuantity).isEqualTo(99);
  }

  @Test
  void concurrentDecreaseStock() throws InterruptedException {
    int threadCount = 100;
    ExecutorService executorService = Executors.newFixedThreadPool(32);
    CountDownLatch countDownLatch = new CountDownLatch(threadCount);

    for (int i = 0; i < threadCount; i++) {
      executorService.submit(() -> {
        try {
//          stockService.decreaseStock(1L, 1L);
//          pessimisticLockStockService.decreaseStock(1L, 1L);
          optimisticLockStockFacade.decreaseStock(1L, 1L);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        } finally {
          countDownLatch.countDown();
        }
      });
    }

    countDownLatch.await();

    Long remainingQuantity = stockRepository.findById(1L).orElseThrow().getQuantity();
//    assertThat(remainingQuantity).isNotZero();
    assertThat(remainingQuantity).isZero();

    /*
    * 1. 여러 쓰레드가 거의 동시에 findById(1L)을 호출 -> 같은 수량(100)을 가진 Stock 객체를 가져옴
    * 2. 각각의 쓰레드가 1을 감소시킨 후 DB에 update
    * 3. DB에는 거의 동시에 다음과 같은 쿼리가 날아감.
    * UPDATE stock SET quantity = 99 WHERE id = 1; (1번 쓰레드)
    * UPDATE stock SET quantity = 99 WHERE id = 1; (2번 쓰레드)
    * 4. 쓰레드들이 최신 값을 모르고 DB에 이전 값(예: 100 -> 99)만을 반영함
    * 5. 이 때문에 마지막에 실행된 몇몇 쓰레드만 실제로 수량을 감소시킴(나머지는 덮어쓰기)
     * */
  }

}