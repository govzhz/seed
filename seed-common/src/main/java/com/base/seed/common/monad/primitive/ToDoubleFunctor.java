package com.base.seed.common.monad.primitive;

import com.base.seed.common.monad.Filter;
import com.base.seed.common.monad.bi.BiBoolOp;
import com.base.seed.common.monad.primitive.pure.CompareDouble;
import java.util.Objects;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;

@FunctionalInterface
public interface ToDoubleFunctor<T> extends java.util.function.ToDoubleFunction<T>, CompareDouble<T> {

  default Double apply(T t) {
    return applyAsDouble(t);
  }

  default Filter<T> filter(DoublePredicate after) {
    Objects.requireNonNull(after);
    return (T t) -> after.test(applyAsDouble(t));
  }

  default <R> java.util.function.Function<T,R> map(DoubleFunction<? extends R> after) {
    Objects.requireNonNull(after);
    return t -> after.apply(this.applyAsDouble(t));
  }

  default DoubleJoiner<T> and() {
    return new DoubleJoiner<>(this, null, BiBoolOp.AND);
  }

  default DoubleJoiner<T> or() {
    return new DoubleJoiner<>(this, null, BiBoolOp.OR);
  }

}
