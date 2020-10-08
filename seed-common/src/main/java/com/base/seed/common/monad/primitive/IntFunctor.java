package com.base.seed.common.monad.primitive;

@FunctionalInterface
public interface IntFunctor<T> extends java.util.function.IntFunction<T> {

  default T apply(Integer v) {
    return apply(v.intValue());
  }

}