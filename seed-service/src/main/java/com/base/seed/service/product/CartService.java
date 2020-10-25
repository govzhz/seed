package com.base.seed.service.product;

import com.base.seed.service.domain.CartItem;
import java.util.List;

public interface CartService {

  /**
   * 获取购物车信息
   */
  List<CartItem> listCartItems(List<Long> cartIds);
}
