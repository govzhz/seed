package com.base.seed.service.domain;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PromotionProductItem {

  /**
   * 商品唯一编号
   */
  private Long productId;

  /**
   * 秒杀价
   */
  private BigDecimal price;

  /**
   * 秒杀商品库存
   */
  private Long stock;
}
