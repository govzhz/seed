package com.base.seed.service.stock;

public interface RemoteStockLifeCycle {

  /**
   * 活动发布
   */
  void onPromotionPublish(Long promotionId);

  /**
   * 活动下线
   */
  void onPromotionOffline(Long promotionId);
}
