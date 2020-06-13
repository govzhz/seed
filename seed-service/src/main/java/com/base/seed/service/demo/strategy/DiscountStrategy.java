package com.base.seed.service.demo.strategy;

import java.math.BigDecimal;


public interface DiscountStrategy {

  /**
   * 获取折扣后的价格
   */
  BigDecimal getDiscount(BigDecimal amount);

  /**
   * 实现类处理身份标志
   */
  IdentityEnum identity();
}
