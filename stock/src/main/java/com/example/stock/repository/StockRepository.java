package com.example.stock.repository;

import com.example.stock.domain.Stock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT s FROM Stock s WHERE s.id = :id")
  Stock findByIdWithPessimisticLock(Long id);

  @Lock(LockModeType.OPTIMISTIC)
  @Query("SELECT s FROM Stock s WHERE s.id = :id")
  Stock findByIdWithOptimisticLock(Long id);

}
