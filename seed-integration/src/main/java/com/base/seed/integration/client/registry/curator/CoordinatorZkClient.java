package com.base.seed.integration.client.registry.curator;

import com.base.seed.integration.client.registry.ZkClient;
import org.apache.curator.framework.recipes.leader.LeaderLatch;

public interface CoordinatorZkClient extends ZkClient {

  /**
   * 是否为leader
   */
  boolean isLeader(LeaderLatch leaderLatch);

  /**
   * 是否为leader，若当前无leader将阻塞等待
   */
  boolean isLeaderUntilBlock(LeaderLatch leaderLatch);

  /**
   * 获取leader唯一编号
   */
  String getLeaderId(LeaderLatch leaderLatch);

  /**
   * 竞争leader
   */
  void electLeader(LeaderLatch leaderLatch);

  /**
   * 事务执行
   */
  void executeInTransaction(TransactionExecutionCallback callback);
}
