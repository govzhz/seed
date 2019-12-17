package com.base.seed.service.demo.strategy;

import java.math.BigDecimal;

/**
 * @author zz 2019/12/16
 */
public interface DiscountStrategy {

    /**
     * 获取折扣后的价格
     *
     * @return
     */
    BigDecimal getDiscount(BigDecimal amount);

    /**
     * 实现类处理身份标志
     * @return
     */
    IdentityEnum identity();
}
