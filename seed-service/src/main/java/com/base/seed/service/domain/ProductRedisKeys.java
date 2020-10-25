package com.base.seed.service.domain;

import com.base.seed.common.constants.GlobalRedisKeys;

public class ProductRedisKeys {

  public static String getStockKey(Long productId) {
    return GlobalRedisKeys.PRODUCT_SKU_STOCK + productId;
  }

  public static String getPromotionKey(Long promotionId) {
    return GlobalRedisKeys.PRODUCT_PROMOTION + promotionId;
  }
}
