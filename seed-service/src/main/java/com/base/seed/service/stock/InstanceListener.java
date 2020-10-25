package com.base.seed.service.stock;

import com.base.seed.common.number.BigDecimalUtil;
import com.base.seed.integration.client.registry.curator.CoordinatorZkClient;
import com.base.seed.integration.client.registry.DataChangeListener;
import com.base.seed.integration.client.registry.curator.TransactionExecutionCallback;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.api.transaction.CuratorOp;
import org.apache.curator.framework.api.transaction.TransactionOp;
import org.apache.curator.framework.recipes.leader.LeaderLatch;

@Slf4j
public class InstanceListener implements DataChangeListener {

  private final CoordinatorZkClient coordinatorZkClient;
  private final LeaderLatch leaderLatch;

  public InstanceListener(CoordinatorZkClient coordinatorZkClient,
      LeaderLatch leaderLatch) {
    this.coordinatorZkClient = coordinatorZkClient;
    this.leaderLatch = leaderLatch;
  }

  @Override
  public void onDataChanged(String path, String value, Type eventType) {
    // CuratorCache 默认使用单线程的线程池，所以不需要同步控制
    // 为防止无 leader 的情况出现事件丢失事件，所有节点均需阻塞等到出现 leader 处理事件
    if (coordinatorZkClient.isLeaderUntilBlock(leaderLatch)) {
      log.info("Instance Sharding start, Event[path={}, value={}, type={}], leaderId={}", path, value, eventType,
          coordinatorZkClient.getLeaderId(leaderLatch));
      List<String> instances = coordinatorZkClient.getChildrenPath(NodePath.getInstanceDirKey());
      List<String> existSharding = coordinatorZkClient.getChildrenPath(NodePath.getShardingDirKey());

      // 事务中不允许出现 No-Exist Node 或 Node Exists，否则无法执行成功，所以预先持久化
      if (!coordinatorZkClient.isExisted(NodePath.getShardingDirKey())) {
        coordinatorZkClient.createPersistentNode(NodePath.getShardingDirKey(), StringUtils.EMPTY);
      }

      coordinatorZkClient.executeInTransaction(new ShardingCallback(existSharding, instances));
      log.info("Instance Sharding completed, Event[path={}, value={}, type={}]", path, value, eventType);
    }
  }

  private static class ShardingCallback implements TransactionExecutionCallback {

    private final List<String> childrenPath;
    private final List<String> instances;

    public ShardingCallback(List<String> childrenPath, List<String> instances) {
      this.childrenPath = childrenPath;
      this.instances = instances;
    }

    @Override
    public List<CuratorOp> execute(TransactionOp transactionOp) throws Exception {
      List<CuratorOp> curatorOps = new LinkedList<>();
      removeShardingNode(curatorOps, transactionOp);
      createShardingNode(curatorOps, transactionOp);
      return curatorOps;
    }

    private void removeShardingNode(List<CuratorOp> curatorOps, TransactionOp transactionOp) throws Exception {
      for (String path : childrenPath) {
        curatorOps.add(transactionOp.delete().forPath(NodePath.getShardingNodeKey(path)));
      }
      curatorOps.add(transactionOp.delete().forPath(NodePath.getShardingDirKey()));
    }

    private void createShardingNode(List<CuratorOp> curatorOps, TransactionOp transactionOp) throws Exception {
      curatorOps.add(transactionOp.create().forPath(NodePath.getShardingDirKey(), StringUtils.EMPTY.getBytes()));
      for (Map.Entry<String, BigDecimal> entry : getShardingRatio(instances).entrySet()) {
        curatorOps.add(
            transactionOp.create().forPath(entry.getKey(), BigDecimalUtil.toString(entry.getValue()).getBytes()));
      }
    }

    private Map<String, BigDecimal> getShardingRatio(List<String> instances) {
      if (CollectionUtils.isEmpty(instances)) {
        return Collections.emptyMap();
      }

      BigDecimal ratio = BigDecimal.ONE.divide(BigDecimal.valueOf(instances.size()), 4, RoundingMode.HALF_UP);
      return instances.stream()
          .collect(Collectors.toMap(NodePath::getShardingNodeKey, value -> ratio));
    }
  }
}
