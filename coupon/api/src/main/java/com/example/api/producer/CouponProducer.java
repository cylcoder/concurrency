package com.example.api.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponProducer {

  private final KafkaTemplate<String, Long> kafkaTemplate;

  public void createCoupon(Long userId) {
    kafkaTemplate.send("create_coupon", userId);
  }

}
