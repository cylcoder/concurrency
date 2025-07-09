package com.example.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.api.repository.CouponRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CouponServiceTest {

  @Autowired
  private CouponService couponService;

  @Autowired
  private CouponRepository couponRepository;

  @Test
  @DisplayName("한명의 사용자가 쿠폰을 응모할 때 정상 발급된다.")
  void singleApply() {
    couponService.applyCoupon(1L);
    assertThat(couponRepository.count()).isOne();
  }

  @Test
  @DisplayName("")
  void concurrentApply() throws InterruptedException {
    int threadCount = 1000;
    ExecutorService executorService = Executors.newFixedThreadPool(32);
    CountDownLatch countDownLatch = new CountDownLatch(threadCount);

    for (int i = 0; i < threadCount; i++) {
      long userId = i + 1;
      executorService.submit(() -> {
        try {
          couponService.applyCoupon(userId);
        } finally {
           countDownLatch.countDown();
        }
      });
    }

    countDownLatch.await();

    assertThat(couponRepository.count()).isGreaterThan(100);
  }

}