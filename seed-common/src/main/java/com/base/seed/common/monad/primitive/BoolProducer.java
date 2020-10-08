package com.base.seed.common.monad.primitive;

import com.base.seed.common.monad.Producer;
import java.util.function.BooleanSupplier;

@FunctionalInterface
public interface BoolProducer extends BooleanSupplier, Producer<Boolean> {

  @Override
  default Boolean get() {
    return getAsBoolean();
  }
}
