package com.example.stock.facade;

import com.example.stock.repository.LockRepository;
import com.example.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class NamedLockStockFacade {

  private final LockRepository lockRepository;
  private final StockService stockService;

  @Transactional
  public void decreaseStock(Long id, Long quantity) {
    try {
      lockRepository.getLock(id.toString());
      stockService.decreaseStock(id, quantity);
    } finally {
      lockRepository.releaseLock(id.toString());
    }
  }

}
