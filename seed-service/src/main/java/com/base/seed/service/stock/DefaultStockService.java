package com.base.seed.service.stock;

import com.base.seed.common.exception.BizException;
import com.base.seed.common.monad.Monad;
import com.base.seed.service.domain.ProductRedisKeys;
import com.base.seed.service.domain.PromotionDetail;
import com.base.seed.service.domain.PromotionProductItem;
import com.base.seed.service.mock.MockService;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultStockService extends AbstractMultiCacheStockService implements RemoteStockLifeCycle {

  protected final RedissonClient redissonClient;

  @Autowired
  private MockService mockService;

  @Autowired
  public DefaultStockService(RedissonClient redissonClient, StockRatioFactory stockRatioFactory) {
    super(stockRatioFactory);
    this.redissonClient = redissonClient;
  }

  @Override
  protected boolean checkProductInPromotion(Long productId, Long promotionId) {
    // TODO: 2020/10/25
    return true;
  }

  @Override
  protected Long getRemoteProductStock(Long productId, Long promotionId) {
    return (Long) getRemotePromotionMap(promotionId).get(productId);
  }

  @Override
  protected boolean deductRemoteStock(Long productId, Long promotionId, Integer delta) {
    return (Long) getRemotePromotionMap(promotionId).addAndGet(productId, -1 * delta) >= 0;
  }

  @Override
  public void onPromotionPublish(Long promotionId) {
    PromotionDetail promotionDetail = Monad.map(mockService::getPromotion)
        .when(promotion -> LocalDateTime.now().isBefore(promotion.getStartTime()))
        .to(Function.identity())
        .apply(promotionId);

    Map<String, Long> productStockMap = promotionDetail.getProducts()
        .stream()
        .collect(Collectors.toMap(product -> ProductRedisKeys.getStockKey(product.getProductId()),
            PromotionProductItem::getStock));
    getRemotePromotionMap(promotionId).putAll(productStockMap);
  }

  @Override
  public void onPromotionOffline(Long promotionId) {
    Optional.ofNullable(mockService.getPromotion(promotionId))
        .filter(PromotionDetail::isEnabled)
        .orElseThrow(
            () -> new BizException(String.format("Promotion[%s] does not exist or is not enabled", promotionId)));

    getRemotePromotionMap(promotionId).clear();
  }

  private RMap<Object, Object> getRemotePromotionMap(Long promotionId) {
    return redissonClient.getMap(ProductRedisKeys.getPromotionKey(promotionId));
  }
}
