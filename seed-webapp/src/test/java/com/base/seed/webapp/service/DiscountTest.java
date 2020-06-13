package com.base.seed.webapp.service;

import com.base.seed.service.demo.strategy.DiscountContext;
import com.base.seed.service.demo.strategy.IdentityEnum;
import com.base.seed.webapp.BaseTest;
import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DiscountTest extends BaseTest {

  @Autowired
  private DiscountContext discountContext;

  @Test
  public void test() {
    Assert.assertEquals(0,
        discountContext.getDiscount(IdentityEnum.NORMAL, new BigDecimal("100")).compareTo(new BigDecimal("80")));
    Assert.assertEquals(0,
        discountContext.getDiscount(IdentityEnum.VIP, new BigDecimal("100")).compareTo(new BigDecimal("50")));
  }
}
