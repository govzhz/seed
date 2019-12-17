package com.base.seed.service.demo.strategy;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zz 2019/12/16
 */
@Component
public class DiscountContext implements ApplicationContextAware, InitializingBean {

    private Map<IdentityEnum, DiscountStrategy> strategyMap = new HashMap<>();

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, DiscountStrategy> beans = applicationContext.getBeansOfType(DiscountStrategy.class);
        for (String beanName : beans.keySet()) {
            strategyMap.put(beans.get(beanName).identity(), beans.get(beanName));
        }
    }

    public BigDecimal getDiscount(IdentityEnum identityEnum, BigDecimal amount){
        return strategyMap.get(identityEnum).getDiscount(amount);
    }
}
