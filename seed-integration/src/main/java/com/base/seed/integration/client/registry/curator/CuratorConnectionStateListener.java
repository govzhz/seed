package com.base.seed.integration.client.registry.curator;


import com.base.seed.integration.client.registry.ConnStatusListener;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;

@Slf4j
public class CuratorConnectionStateListener implements ConnectionStateListener {

  private static final long UNKNOWN = -1L;
  private long lastSessionId;
  private final CuratorFramework client;
  private List<ConnStatusListener> listeners;

  public CuratorConnectionStateListener(CuratorFramework client, List<ConnStatusListener> listeners) {
    this.client = client;
    this.listeners = listeners;
  }

  @Override
  public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
    long currentSessionId = UNKNOWN;

    try {
      currentSessionId = client.getZookeeperClient().getZooKeeper().getSessionId();
    } catch (Exception e) {
      log.warn("Curator client state changed[{}], failed to get zk session id", connectionState, e);
    }

    switch (connectionState) {
      case LOST:
        log.warn("Curator zookeeper session[{}] expired.", currentSessionId);
        publish(ConnStatusListener.SESSION_LOST);
        break;
      case CONNECTED:
        log.info("Curator zookeeper session[{}] connected. ", currentSessionId);
        lastSessionId = currentSessionId;
        publish(ConnStatusListener.CONNECTED);
        break;
      case RECONNECTED:
        if (currentSessionId == UNKNOWN || currentSessionId != lastSessionId) {
          log.warn("Previous session[{}] was lost and a new session[{}] was recreated", lastSessionId, currentSessionId );
          publish(ConnStatusListener.NEW_SESSION_CREATED);
        } else {
          log.warn("Curator zookeeper session[{}] resumed.", currentSessionId);
          publish(ConnStatusListener.RECONNECTED);
        }
        break;
      case SUSPENDED:
        log.warn("Curator zookeeper session[{}] timed out.", currentSessionId);
        publish(ConnStatusListener.SUSPENDED);
        break;
      default:
        log.warn("Curator zookeeper session[{}] status is ignored.", currentSessionId);
        break;
    }
  }

  private void publish(int status) {
    listeners.forEach(listener -> listener.onStateChanged(status));
  }
}
