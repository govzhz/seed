package com.base.seed.common.monad.primitive;

import com.base.seed.common.monad.Producer;
import com.base.seed.common.monad.primitive.pure.LongToLongFunctor;
import java.util.Objects;
import java.util.function.*;

/**
 * <p> Created by pengshuolin on 2018/8/3
 */
@FunctionalInterface
public interface LongProducer extends LongSupplier, Producer<Long> {

  @Override
  default Long get() {
    return getAsLong();
  }

  default <T> Producer<T> mapLong(LongFunction<? extends T> after) {
    Objects.requireNonNull(after);
    return () -> after.apply(getAsLong());
  }

  default IntProducer mapToInt(LongToIntFunction after) {
    Objects.requireNonNull(after);
    return () -> after.applyAsInt(getAsLong());
  }

  default LongProducer mapToLong(LongToLongFunctor after) {
    Objects.requireNonNull(after);
    return () -> after.applyAsLong(getAsLong());
  }

  default DoubleProducer mapToDouble(LongToDoubleFunction after) {
    Objects.requireNonNull(after);
    return () -> after.applyAsDouble(getAsLong());
  }

  default BoolProducer filterLong(LongPredicate filter) {
      return () -> filter.test(getAsLong());
  }
}
