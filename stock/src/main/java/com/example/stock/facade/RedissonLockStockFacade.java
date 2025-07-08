package com.example.stock.facade;

import com.example.stock.service.StockService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedissonLockStockFacade {

  private final RedissonClient redissonClient;
  private final StockService stockService;

  public void decreaseStock(Long id, Long quantity) {
    RLock lock = redissonClient.getLock(String.valueOf(id));

    try {
      boolean isLocked = lock.tryLock(10, 1, TimeUnit.SECONDS);
      if (!isLocked) {
        System.out.println("Locking failed.");
        return;
      }

      stockService.decreaseStock(id, quantity);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } finally {
      lock.unlock();
    }
  }

}
