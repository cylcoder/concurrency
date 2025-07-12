package com.example.api.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisCouponRepository {

  private final RedisTemplate<String, String> redisTemplate;

  public Long incrementCouponCount() {
    return redisTemplate
        .opsForValue()
        .increment("coupon_count");
  }

}
