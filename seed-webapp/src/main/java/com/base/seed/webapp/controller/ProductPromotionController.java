package com.base.seed.webapp.controller;

import com.base.seed.common.PageParam;
import com.base.seed.service.product.ProductPromotionService;
import com.base.seed.service.domain.PromotionProductItem;
import com.base.seed.webapp.support.RestEntity;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("product/promotion")
public class ProductPromotionController {

  @Autowired
  private ProductPromotionService productPromotionService;

  /**
   * 秒杀商品列表
   */
  @GetMapping("items")
  public RestEntity<List<PromotionProductItem>> pagePromotionProducts(PageParam<Long> pageParam) {
    return RestEntity.ok();
  }

  /**
   * 秒杀商品上架
   */
  @PostMapping("publish")
  public RestEntity<Void> publishPromotionProduct(Long promotionId) {
    productPromotionService.publish(promotionId);
    return RestEntity.ok();
  }

  /**
   * 秒杀商品下架
   */
  public RestEntity<Void> offlinePromotionProduct(Long promotionId) {
    productPromotionService.offline(promotionId);
    return RestEntity.ok();
  }
}
