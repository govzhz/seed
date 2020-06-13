package com.base.seed.service.demo.strategy;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class VipDiscountStrategy implements DiscountStrategy {

  @Override
  public BigDecimal getDiscount(BigDecimal amount) {
    return amount.multiply(new BigDecimal("0.5"));
  }

  @Override
  public IdentityEnum identity() {
    return IdentityEnum.VIP;
  }
}
