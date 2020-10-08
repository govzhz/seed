package com.base.seed.common.monad.primitive;

import com.base.seed.common.monad.Producer;
import com.base.seed.common.monad.primitive.pure.IntToIntFunctor;
import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;

/**
 * <p> Created by pengshuolin on 2018/8/3
 */
@FunctionalInterface
public interface IntProducer extends IntSupplier, Producer<Integer> {

  @Override
  default Integer get() {
    return getAsInt();
  }

  default <T> Producer<T> mapInt(java.util.function.IntFunction<? extends T> after) {
    Objects.requireNonNull(after);
    return () -> after.apply(getAsInt());
  }

  default IntProducer mapToInt(IntToIntFunctor after) {
    Objects.requireNonNull(after);
    return () -> after.applyAsInt(getAsInt());
  }

  default LongProducer mapToLong(IntToLongFunction after) {
    Objects.requireNonNull(after);
    return () -> after.applyAsLong(getAsInt());
  }

  default DoubleProducer mapToDouble(IntToDoubleFunction after) {
    Objects.requireNonNull(after);
    return () -> after.applyAsDouble(getAsInt());
  }

  default BoolProducer filterInt(IntPredicate filter) {
    return () -> filter.test(getAsInt());
  }

}
