package com.base.seed.service.stock;

import com.base.seed.common.constants.SeedErrorCode;
import com.base.seed.common.exception.BizException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractMultiCacheStockService implements StockService {

  private final StockRatioFactory stockRatioFactory;
  // productId -> promotionId -> stock
  private final Map<Long, ConcurrentHashMap<Long, AtomicLong>> stockCacheMap = new ConcurrentHashMap<>();

  public AbstractMultiCacheStockService(StockRatioFactory stockRatioFactory) {
    this.stockRatioFactory = stockRatioFactory;
  }

  @Override
  public AtomicLong getStock(Long productId, Long promotionId) {
    if (!checkProductInPromotion(productId, promotionId)) {
      throw new BizException(String.format("Product[%s] not in promotion[%s]", productId, promotionId),
          SeedErrorCode.STOCK_PRODUCT_NOT_IN_PROMOTION);
    }

    ConcurrentHashMap<Long, AtomicLong> promotionMap = stockCacheMap.get(productId);
    if (promotionMap == null) {
      stockCacheMap.putIfAbsent(productId, new ConcurrentHashMap<>());
      promotionMap = stockCacheMap.get(productId);
    }
    AtomicLong stock = promotionMap.get(promotionId);
    if (stock == null) {
      promotionMap.computeIfAbsent(promotionId, value -> calLocalStock(productId, promotionId));
      stock = promotionMap.get(promotionId);
    }
    return stock;
  }

  @Override
  public boolean deductStock(Long productId, Long promotionId, Integer delta) {
    boolean deductSuccess = getStock(productId, promotionId).addAndGet(-1 * delta) >= 0;
    if (deductSuccess) {
      deductSuccess = deductRemoteStock(productId, promotionId, delta);
    }
    return deductSuccess;
  }

  /**
   * 校验商品是否属于该秒杀活动
   */
  protected abstract boolean checkProductInPromotion(Long productId, Long promotionId);

  /**
   * 获取远程商品库存
   */
  protected abstract Long getRemoteProductStock(Long productId, Long promotionId);

  /**
   * 扣减远程商品库存
   */
  protected abstract boolean deductRemoteStock(Long productId, Long promotionId, Integer delta);

  private AtomicLong calLocalStock(Long productId, Long promotionId) {
    Long remoteStock = getRemoteProductStock(productId, promotionId);
    if (remoteStock == null) {
      log.warn("Get Remote Product Stock failed, productId={}", productId);
      return new AtomicLong(0);
    }
    BigDecimal stockRatio = stockRatioFactory.getStockRatio();
    if (stockRatio == null) {
      log.warn("Get Stock ratio failed, productId={}, promotionId={}", productId, promotionId);
      return new AtomicLong(0);
    }
    return new AtomicLong(BigDecimal.valueOf(remoteStock).multiply(stockRatio).longValue());
  }
}
