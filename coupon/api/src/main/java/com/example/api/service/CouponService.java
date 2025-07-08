package com.example.api.service;

import com.example.api.domain.Coupon;
import com.example.api.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {
  
  private final CouponRepository couponRepository;

  public void applyCoupon(Long userId) {
    long count = couponRepository.count();
    if (count < 100) {
      couponRepository.save(new Coupon(userId));
    }
  }

}
