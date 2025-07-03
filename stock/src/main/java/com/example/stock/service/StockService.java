package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockService {

  private final StockRepository stockRepository;

  /*
  * synchronized가 붙어도 재고가 0이 되지 않는 이유
  * 1. 여러 스레드가 동시에 decraseStock 메서드 호출
  * 2. 각 호출은 독립적인 트랜잭션을 시작
  * 3. synchronized로 인해 decreaseStock 메서드는 순차적으로 실행됨
  * 4. 그러나 각 스레드가 decreaseStock 메서드를 실행할 때 자신의 트랜잭션 컨텍스트 안에서 DB를 조회
  * 5. 이전 트랜잭션이 커밋되지 않았거나 격리 수준 때문에 변경 사항을 볼 수 없다면 모든 트랜잭션이 동일한(오래된) 초기 재고 값을 읽음
  * 6. 각 트랜잭션은 오래된 값을 기반으로 재고를 감소시키지만 DB 반영은 트랜잭션이 커밋될 때 발생(트랜잭션 종료될 때 까지 반영 X)
  * 7. 결과적으로 여러 트랜잭션이 동일한 오래된 값을 기반으로 UPDATE 쿼리를 날리고 최종적으로 가장 마지막에 커밋된 트랜잭션의 결과만 반영
  *
  * 쉽게 말해, A가 saveAndFlush 메서드를 호출한 뒤 커밋하기 전 시간동안 B가 findById를 하면 감소되기 전 재고를 읽는 문제 발생
  * */
//  @Transactional
  public synchronized void decreaseStock(Long id, Long quantity) {
    Stock stock = stockRepository.findById(id).orElseThrow();
    stock.decrease(quantity);
    stockRepository.saveAndFlush(stock);
  }

}
