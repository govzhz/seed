package com.base.seed.service.product;

import com.base.seed.service.domain.CartItem;
import com.base.seed.service.stock.RemoteStockLifeCycle;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public interface ProductOrderService {

  /**
   * 根据被选中的购物车商品唯一编号生成订单
   */
  void createOrder(List<CartItem> cartItems);

  @Slf4j
  @Service class ProductOrderServiceImpl implements ProductOrderService {

    @Override
    public void createOrder(List<CartItem> cartItems) {
    }
  }

  @Slf4j
  @Service class ProductPromotionServiceImpl implements ProductPromotionService {

    @Autowired
    private RemoteStockLifeCycle remoteStockLifeCycle;

    @Override
    public void publish(Long promotionId) {
      remoteStockLifeCycle.onPromotionPublish(promotionId);
    }

    @Override
    public void offline(Long promotionId) {
      remoteStockLifeCycle.onPromotionOffline(promotionId);
    }
  }
}
