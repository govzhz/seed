package com.base.seed.integration.client.registry;

import java.util.List;

public interface ZkClient {

  /**
   * 获取节点数据
   *
   *  若不存在节点将返回空字符串
   */
  String getData(String nodePath);

  /**
   * 创建临时节点
   *
   *  若已存在临时节点将删除重新创建
   */
  void createEphemeralNode(String nodePath, String value);

  /**
   * 创建持久化节点
   *
   *  若已存在持久节点将更新节点值
   */
  void createPersistentNode(String nodePath, String value);

  /**
   * 添加连接状态监听器
   */
  void addConnStatusListener(ConnStatusListener listener);

  /**
   * 添加节点变化监听器
   */
  void addDataChangeListener(String nodePath, DataChangeListener listener);

  /**
   * 节点是否存在
   */
  boolean isExisted(String nodePath);

  /**
   * 删除节点
   *
   *  若节点不存在将不做任何处理
   */
  void deleteNode(String nodePath);

  /**
   * 获取子路径列表
   */
  List<String> getChildrenPath(String nodePath);

  /**
   * 获取客户端
   */
  Object getClient();

  /**
   * 关闭连接
   */
  void close();

  /**
   * 获取注册中心时间
   */
  long getRegistryCenterTime(String nodePath);
}
