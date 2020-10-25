package com.base.seed.common.env;

import java.io.IOException;

public final class HostException extends RuntimeException {

  public HostException(final IOException cause) {
    super(cause);
  }

  public HostException(final String message) {
    super(message);
  }
}
