package com.base.seed.service.mock;

import com.base.seed.service.domain.PromotionDetail;
import com.base.seed.service.domain.PromotionProductItem;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Service;

@Service
public class MockService {

  // productId -> PromotionProductBo
  private final Map<Long, PromotionProductItem> promotionProductMap = new ConcurrentHashMap<>();
  // promotionId -> PromotionDetailBo
  private final Map<Long, PromotionDetail> promotionDetailMap = new ConcurrentHashMap<>();

  public PromotionDetail getPromotion(Long promotionId) {
    PromotionDetail promotionDetail = promotionDetailMap.get(promotionId);
    if (promotionDetail == null) {
      PromotionDetail newPromotionDetail = new PromotionDetail();
      newPromotionDetail.setId(promotionId);
      newPromotionDetail.setName("秒杀清仓活动");
      newPromotionDetail.setStartTime(LocalDateTime.of(2020, 10, 10, 8, 10, 0));
      newPromotionDetail.setStartTime(LocalDateTime.of(2020, 10, 10, 9, 10, 0));
      newPromotionDetail.setProducts(Collections.singletonList(getProduct(1L)));
      newPromotionDetail.setFinished(false);
      promotionDetailMap.putIfAbsent(promotionId, newPromotionDetail);
      promotionDetail = promotionDetailMap.get(promotionId);
    }
    return promotionDetail;
  }

  public PromotionProductItem getProduct(Long productId) {
    PromotionProductItem promotionProduct = promotionProductMap.get(productId);
    if (promotionProduct == null) {
      PromotionProductItem newProduct = new PromotionProductItem();
      newProduct.setPrice(BigDecimal.valueOf(100));
      newProduct.setProductId(productId);
      newProduct.setStock(ThreadLocalRandom.current().nextLong(1, 1000));
      promotionProductMap.putIfAbsent(productId, newProduct);
      promotionProduct = promotionProductMap.get(productId);
    }
    return promotionProduct;
  }
}
