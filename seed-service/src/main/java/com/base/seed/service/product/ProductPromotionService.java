package com.base.seed.service.product;

public interface ProductPromotionService {

  /**
   * 秒杀活动上线
   */
  void publish(Long promotionId);

  /**
   * 秒杀活动下线
   */
  void offline(Long promotionId);
}
