package com.base.seed.service.stock;

import java.util.concurrent.atomic.AtomicLong;

public interface StockService {

  /**
   * 获取指定商品在指定秒杀活动下的库存
   */
  AtomicLong getStock(Long productId, Long promotionId);

  /**
   * 扣减指定商品在指定秒杀活动下的库存
   */
  boolean deductStock(Long productId, Long promotionId, Integer delta);
}
