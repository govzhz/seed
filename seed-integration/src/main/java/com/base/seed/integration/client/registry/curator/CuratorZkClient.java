package com.base.seed.integration.client.registry.curator;

import com.base.seed.common.exception.RegistryException;
import com.base.seed.common.properties.ZookeeperProperties;
import com.base.seed.integration.client.registry.ConnStatusListener;
import com.base.seed.integration.client.registry.DataChangeListener;
import com.base.seed.integration.client.registry.ZkClient;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CuratorZkClient implements ZkClient {

  private final CuratorFramework client;
  private final List<ConnStatusListener> connStatusListeners = new CopyOnWriteArrayList<>();
  private final Map<String, CuratorCache> caches = new ConcurrentHashMap<>();

  @Autowired
  public CuratorZkClient(ZookeeperProperties properties) {
    try {
      CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
          .connectString(properties.getServerLists())
          .retryPolicy(new ExponentialBackoffRetry(
              properties.getBaseSleepTimeMilliseconds(), properties.getMaxRetries(),
              properties.getMaxSleepTimeMilliseconds()))
          .namespace(properties.getNamespace());
      client = builder.build();
      client.getConnectionStateListenable()
          .addListener(new CuratorConnectionStateListener(client, connStatusListeners));
      client.start();
      boolean connected = client.blockUntilConnected(
          properties.getMaxSleepTimeMilliseconds() * properties.getMaxRetries(), TimeUnit.MILLISECONDS);
      if (!connected) {
        throw new RegistryException("zookeeper not connected");
      }
    } catch (RegistryException ex) {
      throw ex;
    } catch (Exception e) {
      throw new RegistryException(e.getMessage(), e);
    }
  }

  @Override
  public String getData(String nodePath) {
    String content = StringUtils.EMPTY;
    try {
      content = new String(client.getData().forPath(nodePath), StandardCharsets.UTF_8);
    } catch (NoNodeException ex) {
      log.warn("Get data from a non-existent node[{}]", nodePath);
    } catch (Exception e) {
      throw new RegistryException(String.format("Get node[%s] value failed.", nodePath), e);
    }
    return content;
  }

  @Override
  public void createEphemeralNode(String nodePath, String value) {
    try {
      client.create()
          .creatingParentsIfNeeded()
          .withMode(CreateMode.EPHEMERAL)
          .forPath(nodePath, value.getBytes(StandardCharsets.UTF_8));
    } catch (NodeExistsException ex) {
      log.warn("Due to the creation of a ephemeral node of the same path[{}],"
          + " the original node[value={}] will be deleted soon", nodePath, getData(nodePath));
      deleteNode(nodePath);
      createEphemeralNode(nodePath, value);
    } catch (Exception e) {
      throw new RegistryException(String.format("Create Ephemeral Node[%s] failed.", nodePath), e);
    }
  }

  @Override
  public void createPersistentNode(String nodePath, String value) {
    try {
      client.create()
          .creatingParentsIfNeeded()
          .withMode(CreateMode.PERSISTENT)
          .forPath(nodePath, value.getBytes(StandardCharsets.UTF_8));
    } catch (NodeExistsException ex) {
      try {
        client.setData().forPath(nodePath, value.getBytes(StandardCharsets.UTF_8));
      } catch (Exception e1) {
        throw new RegistryException(String.format("Create persistent node[%s] failed.", nodePath), e1);
      }
    } catch (Exception e) {
      throw new RegistryException(String.format("Create persistent node[%s] failed.", nodePath), e);
    }
  }

  @Override
  public void addConnStatusListener(ConnStatusListener listener) {
    connStatusListeners.add(listener);
  }

  @Override
  public void addDataChangeListener(String nodePath, DataChangeListener listener) {
    CuratorCache cache = caches.get(nodePath);
    if (cache == null) {
      CuratorCache newCache = CuratorCache.build(client, nodePath);
      caches.putIfAbsent(nodePath, newCache);
      cache = caches.get(nodePath);
      newCache.start();
    }
    cache.listenable().addListener(new CuratorDataChangeListener(listener));
  }

  @Override
  public boolean isExisted(String nodePath) {
    try {
      return client.checkExists().forPath(nodePath) != null;
    } catch (Exception e) {
      throw new RegistryException(String.format("Check node[%s] exists failed.", nodePath), e);
    }
  }

  @Override
  public void deleteNode(String nodePath) {
    try {
      client.delete().deletingChildrenIfNeeded().forPath(nodePath);
    } catch (NoNodeException ex) {
      log.warn("Delete a non-existent node[{}]", nodePath);
    } catch (Exception e) {
      throw new RegistryException(String.format("Delete node[%s] failed.", nodePath), e);
    }
  }

  @Override
  public List<String> getChildrenPath(String nodePath) {
    List<String> nodePaths = new ArrayList<>();
    try {
      nodePaths = client.getChildren().forPath(nodePath);
    } catch (NoNodeException ex) {
      log.warn("Get a non-existent node[{}] children path", nodePath);
    } catch (Exception e) {
      throw new RegistryException(String.format("Get children path failed, nodePath=%s", nodePath), e);
    }
    return nodePaths;
  }

  @Override
  public Object getClient() {
    return client;
  }

  @Override
  public void close() {
    caches.forEach((key, value) -> value.close());
    try {
      TimeUnit.MILLISECONDS.sleep(500);
    } catch (InterruptedException e) {
      log.warn("Sleep interrupted", e);
    }
    client.close();
  }

  @Override
  public long getRegistryCenterTime(String nodePath) {
    long registryCenterTime = -1L;
    try {
      registryCenterTime = Optional.ofNullable(client.checkExists().forPath(nodePath))
          .map(Stat::getMtime).orElse(-1L);
    } catch (NoNodeException ex) {
      log.warn("Get a non-existent node[{}] registry center time", nodePath);
    } catch (Exception e) {
      throw new RegistryException(String.format("Get registry center time failed, nodePath=%s", nodePath), e);
    }
    return registryCenterTime;
  }
}
