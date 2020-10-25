package com.base.seed.service.stock;

import com.base.seed.integration.client.registry.curator.CoordinatorZkClient;
import javax.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StockRadioStarter implements ApplicationListener<ApplicationStartedEvent> {

  @Autowired
  private CoordinatorZkClient coordinatorZkClient;

  @Autowired
  private RatioShardingListener ratioShardingListener;

  @Override
  public void onApplicationEvent(@Nonnull ApplicationStartedEvent applicationStartedEvent) {
    log.info("Stock radio init start.");

    LeaderLatch leaderLatch = getLeaderLatch();
    registerListeners(leaderLatch);
    electLeader(leaderLatch);
    createInstanceNode();

    log.info("Stock radio init completed, currentNode={}", NodePath.getInstanceNodeKey());
  }

  private LeaderLatch getLeaderLatch() {
    String lockNodeKey = NodePath.getLockNodeKey();
    return new LeaderLatch((CuratorFramework) coordinatorZkClient.getClient(), lockNodeKey,
        NodePath.getInstanceNodeKey());
  }

  private void registerListeners(LeaderLatch leaderLatch) {
    coordinatorZkClient.addConnStatusListener(ratioShardingListener);
    coordinatorZkClient.addDataChangeListener(NodePath.getInstanceDirKey(),
        new InstanceListener(coordinatorZkClient, leaderLatch));
    coordinatorZkClient.addDataChangeListener(NodePath.getShardingDirKey(), ratioShardingListener);
  }

  private void electLeader(LeaderLatch leaderLatch) {
    coordinatorZkClient.electLeader(leaderLatch);
    coordinatorZkClient.isLeaderUntilBlock(leaderLatch);
    log.info("Elect leader completed, currentNode is leader? {}", coordinatorZkClient.isLeader(leaderLatch));
  }

  private void createInstanceNode() {
    coordinatorZkClient.createEphemeralNode(NodePath.getInstanceNodeKey(), StringUtils.EMPTY);
  }
}
