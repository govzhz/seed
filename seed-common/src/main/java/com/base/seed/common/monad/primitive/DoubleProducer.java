package com.base.seed.common.monad.primitive;

import com.base.seed.common.monad.Producer;
import com.base.seed.common.monad.primitive.pure.DoubleToDoubleFunctor;
import java.util.Objects;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;

/**
 * <p> Created by pengshuolin on 2018/8/3
 */
@FunctionalInterface
public interface DoubleProducer extends DoubleSupplier, Producer<Double> {

  @Override
  default Double get() {
    return getAsDouble();
  }

  default <T> Producer<T> mapDouble(DoubleFunction<? extends T> after) {
    Objects.requireNonNull(after);
    return () -> after.apply(getAsDouble());
  }

  default IntProducer mapToInt(DoubleToIntFunction after) {
    Objects.requireNonNull(after);
    return () -> after.applyAsInt(getAsDouble());
  }

  default LongProducer mapToLong(DoubleToLongFunction after) {
    Objects.requireNonNull(after);
    return () -> after.applyAsLong(getAsDouble());
  }

  default DoubleProducer mapToDouble(DoubleToDoubleFunctor after) {
    Objects.requireNonNull(after);
    return () -> after.applyAsDouble(getAsDouble());
  }

  default BoolProducer filterDouble(DoublePredicate filter) {
      return () -> filter.test(getAsDouble());
  }
}
