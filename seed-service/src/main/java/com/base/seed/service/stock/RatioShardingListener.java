package com.base.seed.service.stock;

import com.base.seed.integration.client.registry.ConnStatusListener;
import com.base.seed.integration.client.registry.curator.CoordinatorZkClient;
import com.base.seed.integration.client.registry.DataChangeListener;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RatioShardingListener implements StockRatioFactory, DataChangeListener, ConnStatusListener {

  private final CoordinatorZkClient coordinatorZkClient;
  private final AtomicReference<BigDecimal> ratio = new AtomicReference<>(BigDecimal.ZERO);

  public RatioShardingListener(CoordinatorZkClient coordinatorZkClient) {
    this.coordinatorZkClient = coordinatorZkClient;
  }

  @Override
  public BigDecimal getStockRatio() {
    return ratio.get();
  }

  @Override
  public void onDataChanged(String path, String value, Type eventType) {
    // 仅监控 /sharding 事件，避免子节点导致的重复分片
    if (NodePath.getShardingDirKey().equals(path) && eventType.equals(Type.NODE_CREATED)) {
      log.info("Local Refresh ratio start, Event[path={}, value={}, type={}]", path, value, eventType);
      BigDecimal ratio = refreshRatio();
      log.info("Local Refresh ratio completed, updateRadio={}, Event[path={}, value={}, type={}]",
          ratio, path, value, eventType);
    }
  }

  @Override
  public void onStateChanged(int currentState) {
    switch (currentState) {
      case ConnStatusListener.RECONNECTED:
        // 此时 session 不变，暂时不作任何处理
        break;
      case ConnStatusListener.SUSPENDED:
      case ConnStatusListener.SESSION_LOST:
        // 丢失连接不作任何处理，直接使用本地缓存
        break;
      case ConnStatusListener.NEW_SESSION_CREATED:
        // 此时 session 已变，可能丢失先前 session 的事件，刷新比例
        // 由于 session 变更，原先创建的临时节点就会消失，所以需要重新创建
        log.info("Connection[NEW_SESSION_CREATED] Recreate instance node: {}", NodePath.getInstanceNodeKey());
        coordinatorZkClient.createEphemeralNode(NodePath.getInstanceNodeKey(), StringUtils.EMPTY);
        log.info("Connection[NEW_SESSION_CREATED] Refresh ratio start");
        BigDecimal ratio = refreshRatio();
        log.info("Connection[NEW_SESSION_CREATED] Refresh ratio start, updateRatio={}", ratio);
      default:
    }
  }

  private BigDecimal refreshRatio() {
    String date = coordinatorZkClient.getData(NodePath.getShardingNodeKey());
    BigDecimal updateRadio = StringUtils.isNotBlank(date) ? new BigDecimal(date) : BigDecimal.ZERO;
    ratio.set(updateRadio);
    return updateRadio;
  }
}
