package com.base.seed.service.demo.strategy;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author zz 2019/12/16
 */
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
