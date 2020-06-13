package com.base.seed.service.demo.strategy;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class DiscountContext implements ApplicationContextAware, InitializingBean {

  private Map<IdentityEnum, DiscountStrategy> strategyMap;
  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    strategyMap = applicationContext.getBeansOfType(DiscountStrategy.class)
        .values()
        .stream()
        .collect(Collectors.toMap(DiscountStrategy::identity, discountStrategy -> discountStrategy));
  }

  public BigDecimal getDiscount(IdentityEnum identityEnum, BigDecimal amount) {
    return strategyMap.get(identityEnum).getDiscount(amount);
  }
}
