package com.example.stock.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class StockServiceTest {

  @Autowired
  private StockService stockService;

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

}