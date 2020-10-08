package com.base.seed.common.monad.primitive;

@FunctionalInterface
public interface LongFunctor<T> extends java.util.function.LongFunction<T> {

  default T apply(Long v) {
      return apply(v.longValue());
  }
}

