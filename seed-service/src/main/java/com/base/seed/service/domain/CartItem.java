package com.base.seed.service.domain;

import lombok.Data;

@Data
public class CartItem {

  /**
   * 秒杀活动唯一编号
   */
  private Long promotionId;

  /**
   * 商品唯一编号
   */
  private Long productId;

  /**
   * 购买数量
   */
  private Integer quantity;
}
