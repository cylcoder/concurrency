package com.example.api.service;

import com.example.api.domain.Coupon;
import com.example.api.producer.CouponProducer;
import com.example.api.repository.CouponRepository;
import com.example.api.repository.RedisCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {
  
  private final CouponRepository couponRepository;
  private final RedisCouponRepository redisCouponRepository;
  private final CouponProducer couponProducer;

  public void applyCoupon(Long userId) {
//    long count = couponRepository.count();

    long count = redisCouponRepository.incrementCouponCount();

    /*if (count <= 100) {
      couponRepository.save(new Coupon(userId));
    }*/

    couponProducer.createCoupon(userId);
  }

}
