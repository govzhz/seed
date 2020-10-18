package com.base.seed.integration.client.registry;

public interface DataChangeListener {

  enum Type {
    NODE_CREATED,
    NODE_CHANGED,
    NODE_DELETED
  }

  void onDataChanged(String path, String value, Type eventType);
}
