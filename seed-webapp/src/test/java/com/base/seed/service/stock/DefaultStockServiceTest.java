package com.base.seed.service.stock;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultStockServiceTest {

  @Mock
  private StockRatioFactory stockRatioFactory;

  @Mock
  private RedissonClient redissonClient;

  @Mock
  private RMap<Object, Object> rMap;

  @InjectMocks
  private DefaultStockService defaultStockService;

  @Before
  public void setUp() {
    when(redissonClient.getMap(any())).thenReturn(rMap);

    // product=0的库存为0
    when(rMap.get(0L)).thenReturn(0L);
    // 无product=1
    when(rMap.get(1L)).thenReturn(null);
    // product=2的库存为2
    when(rMap.get(2L)).thenReturn(2L);

    // 预设2台服务器均分
    when(stockRatioFactory.getStockRatio()).thenReturn(BigDecimal.valueOf(0.5));
  }

  @Test
  public void testZeroStockRatio() {
    when(stockRatioFactory.getStockRatio()).thenReturn(BigDecimal.ZERO);
    AtomicLong stock = defaultStockService.getStock(0L, 1L);

    verify(redissonClient, atMost(1)).getMap(any(String.class));
    Assert.assertEquals(stock.get(), 0);
  }

  @Test
  public void testGetZeroStock() {
    AtomicLong stock = defaultStockService.getStock(0L, 1L);
    defaultStockService.getStock(0L, 1L);
    defaultStockService.getStock(0L, 1L);

    verify(redissonClient, atMost(1)).getMap(any(String.class));
    Assert.assertEquals(stock.get(), 0);
  }

  @Test
  public void testGetLocalOneTotalTwoStock() {
    AtomicLong stock = defaultStockService.getStock(2L, 1L);
    defaultStockService.getStock(2L, 1L);
    defaultStockService.getStock(2L, 1L);

    verify(redissonClient, atMost(1)).getMap(any(String.class));
    Assert.assertEquals(stock.get(), 1);
  }

  @Test
  public void testNullProduct() {
    AtomicLong stock = defaultStockService.getStock(1L, 1L);
    defaultStockService.getStock(1L, 1L);
    defaultStockService.getStock(1L, 1L);

    verify(redissonClient, atMost(1)).getMap(any(String.class));
    Assert.assertEquals(stock.get(), 0);
  }

  @Test
  public void testDeductStock() {
    when(rMap.addAndGet(2L, -1)).thenReturn(0L);
    Assert.assertTrue(defaultStockService.deductStock(2L, 2L, 1));
    Assert.assertFalse(defaultStockService.deductStock(2L, 2L, 1));
    // 最多只会远程扣减一次
    verify(rMap, atMost(1)).addAndGet(any(), any());
  }
}