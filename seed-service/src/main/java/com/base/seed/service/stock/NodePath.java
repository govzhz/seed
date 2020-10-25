package com.base.seed.service.stock;

import com.base.seed.common.env.IpUtils;
import java.lang.management.ManagementFactory;

public class NodePath {

  private static final String ROOT = "/seed";
  private static final String INSTANCE = ROOT + "/instance";
  private static final String SHARDING = ROOT + "/sharding";
  private static final String LATCH_LEADER = ROOT + "/latch";

  public static String getLockNodeKey() {
    return LATCH_LEADER;
  }

  public static String getInstanceDirKey() {
    return INSTANCE;
  }

  public static String getInstanceNodeKey(String subNodeName) {
    return String.format("%s/%s", INSTANCE, subNodeName);
  }

  public static String getInstanceNodeKey() {
    return String.format("%s/%s", INSTANCE,
        IpUtils.getIp() + "@-@" + ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
  }

  public static String getShardingNodeKey(String subNodeName) {
    return String.format("%s/%s", SHARDING, subNodeName);
  }

  public static String getShardingDirKey() {
    return SHARDING;
  }

  public static String getShardingNodeKey() {
    return String.format("%s/%s", SHARDING,
        IpUtils.getIp() + "@-@" + ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
  }
}
