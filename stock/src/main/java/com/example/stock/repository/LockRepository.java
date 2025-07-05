package com.example.stock.repository;

import com.example.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LockRepository extends JpaRepository<Stock, Long> {

  @Query(value = "SELECT GET_LOCK(:key, 3000)", nativeQuery = true)
  void getLock(String key);

  @Query(value = "SELECT RELEASE_LOCK(:key)", nativeQuery = true)
  void releaseLock(String key);

}
