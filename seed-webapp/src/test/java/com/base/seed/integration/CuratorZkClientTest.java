package com.base.seed.integration;

import com.base.seed.common.properties.ZookeeperProperties;
import com.base.seed.integration.client.registry.curator.CuratorCoordinatorZkClient;
import com.base.seed.service.stock.NodePath;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CuratorZkClientTest {

  private CuratorCoordinatorZkClient curatorZkClient;

  private static final String ROOT = "/seed";

  @Before
  public void setUp() {
    curatorZkClient = new CuratorCoordinatorZkClient(getZKProperties());
  }

  @After
  public void close() {
    curatorZkClient.close();
  }

  /**
   * 新增临时节点
   */
  @Test
  public void testCreateEphemeralNode() {
    curatorZkClient.createEphemeralNode(getPath("test_path"), "test_value");
    Assert.assertEquals(curatorZkClient.getData(getPath("test_path")), "test_value");

    curatorZkClient.createEphemeralNode(getPath("test_path"), "test_value_exist");
    Assert.assertEquals(curatorZkClient.getData(getPath("test_path")), "test_value_exist");
  }

  /**
   * 新增持久化节点
   */
  @Test
  public void testCreatePersistentNode() {
    curatorZkClient.createPersistentNode(getPath("test_path"), "test_value");
    Assert.assertEquals(curatorZkClient.getData(getPath("test_path")), "test_value");

    curatorZkClient.createPersistentNode(getPath("test_path"), "test_value_exist");
    Assert.assertEquals(curatorZkClient.getData(getPath("test_path")), "test_value_exist");
  }

  /**
   * 节点是否存在
   */
  @Test
  public void testIsExisted() {
    curatorZkClient.createEphemeralNode(getPath("test_path_exist"), "test_value");
    Assert.assertTrue(curatorZkClient.isExisted(getPath("test_path_exist")));
    Assert.assertFalse(curatorZkClient.isExisted(getPath("test_path_non_exist")));
  }

  /**
   * 获取当前节点的更新时间
   */
  @Test
  public void testGetRegistryCenterTime() {
    curatorZkClient.createEphemeralNode(getPath("test_path_exist"), "test_value");
    Assert.assertTrue(curatorZkClient.getRegistryCenterTime(getPath("test_path_exist")) > 0);
    Assert.assertEquals(curatorZkClient.getRegistryCenterTime(getPath("test_path_non_exist")), -1);
  }

  /**
   * 删除节点
   */
  @Test
  public void testDeleteNode() {
    curatorZkClient.createPersistentNode(getPath("test_path_exist"), "test_value");
    curatorZkClient.deleteNode(getPath("test_path_exist"));
    Assert.assertFalse(curatorZkClient.isExisted(getPath("test_path_exist")));

    curatorZkClient.deleteNode(getPath("test_path_non_exist"));
    Assert.assertFalse(curatorZkClient.isExisted(getPath("test_path_non_exist")));
  }

  /**
   * 获取一级子目录
   */
  @Test
  public void testGetChildrenPath() {
    curatorZkClient.deleteNode(ROOT);
    Assert.assertEquals(curatorZkClient.getChildrenPath(ROOT).size(), 0);
    curatorZkClient.createEphemeralNode(getPath("test_path"), "test_value");
    Assert.assertTrue(curatorZkClient.getChildrenPath(ROOT).size() > 0);
  }

  /**
   * 监听数据变化
   */
  @Test
  public void testAddDataChangeListener() throws InterruptedException {
    TestDataChangeListener listener = new TestDataChangeListener();
    curatorZkClient.addDataChangeListener(ROOT, listener);
    curatorZkClient.createPersistentNode(getPath("test_path"), "test_value");
    curatorZkClient.createPersistentNode(getPath("test_path"), "test_value2");
    curatorZkClient.deleteNode(getPath("test_path"));
    TimeUnit.MILLISECONDS.sleep(1000);
    Assert.assertTrue(listener.isSuccess());
  }

  /**
   * 选主
   */
  @Test
  public void testElectLeader() {
    LeaderLatch leaderLatch =
        new LeaderLatch((CuratorFramework) curatorZkClient.getClient(), NodePath.getLockNodeKey());
    curatorZkClient.electLeader(leaderLatch);
    Assert.assertTrue(curatorZkClient.isLeader(leaderLatch));
  }

  private String getPath(String nodePath) {
    return new StringJoiner("/")
        .add(ROOT)
        .add(Thread.currentThread().getStackTrace()[2].getMethodName())
        .add(nodePath)
        .toString();
  }

  private ZookeeperProperties getZKProperties() {
    ZookeeperProperties properties = new ZookeeperProperties();
    properties.setServerLists("127.0.0.1:2181");
    properties.setNamespace("seed-test");
    properties.setMaxRetries(3);
    properties.setBaseSleepTimeMilliseconds(1000);
    properties.setMaxSleepTimeMilliseconds(3000);
    return properties;
  }
}
