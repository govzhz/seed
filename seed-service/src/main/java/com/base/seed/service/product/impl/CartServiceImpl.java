package com.base.seed.service.product.impl;

import com.base.seed.service.product.CartService;
import com.base.seed.service.domain.CartItem;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CartServiceImpl implements CartService {

  @Override
  public List<CartItem> listCartItems(List<Long> cartIds) {
    return null;
  }
}
