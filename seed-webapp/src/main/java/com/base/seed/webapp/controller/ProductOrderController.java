package com.base.seed.webapp.controller;

import com.base.seed.service.product.ProductOrderService;
import com.base.seed.webapp.support.RestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("product/order")
public class ProductOrderController {

  @Autowired
  private ProductOrderService productOrderService;

  /**
   * 根据购物车信息下单
   */
  @PostMapping
  public RestEntity<Void> createOrder() {
    return RestEntity.ok();
  }
}
