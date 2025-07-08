package com.example.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.api.repository.CouponRepository;
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
  void applyCoupon() {
    couponService.applyCoupon(1L);
    assertThat(couponRepository.count()).isOne();
  }

}