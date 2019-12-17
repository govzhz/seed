package com.base.seed.webapp;

import com.base.seed.service.demo.strategy.DiscountContext;
import com.base.seed.service.demo.strategy.IdentityEnum;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * @author zz 2019/12/16
 */
public class DiscountTest extends BaseTest {

    @Autowired
    private DiscountContext discountContext;

    @Test
    public void test(){
        Assert.assertEquals(0, discountContext.getDiscount(IdentityEnum.NORMAL, new BigDecimal("100")).compareTo(new BigDecimal("80")));
        Assert.assertEquals(0, discountContext.getDiscount(IdentityEnum.VIP, new BigDecimal("100")).compareTo(new BigDecimal("50")));
    }
}
