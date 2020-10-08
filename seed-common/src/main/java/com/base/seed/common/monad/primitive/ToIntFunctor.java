package com.base.seed.common.monad.primitive;

import com.base.seed.common.monad.Filter;
import com.base.seed.common.monad.bi.BiBoolOp;
import com.base.seed.common.monad.primitive.pure.CompareInt;
import java.util.Objects;
import java.util.function.IntPredicate;

@FunctionalInterface
public interface ToIntFunctor<T> extends java.util.function.ToIntFunction<T>, CompareInt<T> {

  default Integer apply(T t) {
    return applyAsInt(t);
  }

  default Filter<T> filter(IntPredicate after) {
    Objects.requireNonNull(after);
    return (T t) -> after.test(applyAsInt(t));
  }

  default <R> java.util.function.Function<T,R> map(java.util.function.IntFunction<? extends R> after) {
    Objects.requireNonNull(after);
    return t -> after.apply(this.applyAsInt(t));
  }

  default IntJoiner<T> and() {
    return new IntJoiner<>(this, null, BiBoolOp.AND);
  }

  default IntJoiner<T> or() {
    return new IntJoiner<>(this, null, BiBoolOp.OR);
  }

}
