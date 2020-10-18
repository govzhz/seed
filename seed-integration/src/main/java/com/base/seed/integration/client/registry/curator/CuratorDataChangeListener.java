package com.base.seed.integration.client.registry.curator;

import com.alibaba.fastjson.JSON;
import com.base.seed.integration.client.registry.DataChangeListener;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;

@Slf4j
public class CuratorDataChangeListener implements CuratorCacheListener {

  private final DataChangeListener dataChangeListener;

  public CuratorDataChangeListener(DataChangeListener dataChangeListener) {
    this.dataChangeListener = dataChangeListener;
  }

  @Override
  public void event(Type type, ChildData oldData, ChildData newData) {
    if (dataChangeListener == null) {
      log.warn("Data change listener is null, data change event[{}] child is ignored", type);
      return;
    }

    if (oldData == null && newData == null) {
      log.warn("Data change, but oldData and newData is null, type={}, oldData={}, newData={}",
          type, JSON.toJSONString(oldData), JSON.toJSONString(newData));
      return;
    }

    String path = Type.NODE_DELETED.equals(type) ? Optional.ofNullable(oldData).map(ChildData::getPath).orElse(StringUtils.EMPTY)
            : Optional.ofNullable(newData).map(ChildData::getPath).orElse(StringUtils.EMPTY);
    byte[] data = Type.NODE_DELETED.equals(type) ? Optional.ofNullable(oldData).map(ChildData::getData).orElse(null)
        : Optional.ofNullable(newData).map(ChildData::getData).orElse(null);
    if (StringUtils.isBlank(path)) {
      log.warn("Data change, but path is null, type={}, oldData={}, newData={}",
          type, JSON.toJSONString(oldData), JSON.toJSONString(newData));
      return;
    }

    String content = data == null ? StringUtils.EMPTY : new String(data, StandardCharsets.UTF_8);
    DataChangeListener.Type event;
    switch (type) {
      case NODE_CREATED:
        log.warn("Node created, path={}, content={}", path, content);
        event = DataChangeListener.Type.NODE_CREATED;
        break;
      case NODE_CHANGED:
        log.warn("Node changed, path={}, content={}", path, content);
        event = DataChangeListener.Type.NODE_CHANGED;
        break;
      case NODE_DELETED:
        log.warn("Node deleted, path={}, content={}", path, content);
        event = DataChangeListener.Type.NODE_DELETED;
        break;
      default:
        log.warn("Unknown Node Event, path={}, content={}", path, content);
        return;
    }
    dataChangeListener.onDataChanged(path, content, event);
  }
}
