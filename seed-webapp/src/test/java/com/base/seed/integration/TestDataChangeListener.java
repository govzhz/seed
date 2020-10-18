package com.base.seed.integration;

import com.base.seed.integration.client.registry.DataChangeListener;

public class TestDataChangeListener implements DataChangeListener {

  private boolean created;
  private boolean updated;
  private boolean deleted;

  @Override
  public void onDataChanged(String path, String value, Type eventType) {
    switch (eventType) {
      case NODE_CREATED:
        created = true;
        break;
      case NODE_CHANGED:
        updated = true;
        break;
      case NODE_DELETED:
        deleted = true;
        break;
    }
  }

  public boolean isSuccess() {
    return created && updated && deleted;
  }
}
