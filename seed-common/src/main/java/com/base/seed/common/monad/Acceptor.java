package com.base.seed.common.monad;

import java.util.Objects;

@FunctionalInterface
public interface Acceptor<T> extends java.util.function.Consumer<T> {

  default Acceptor<T> then(Acceptor<? super T> after) {
    Objects.requireNonNull(after);
    return (T t) -> {
      accept(t);
      after.accept(t);
    };
  }

  default Acceptor<T> map(Functor<? super T, ? extends T> functor) {
    Objects.requireNonNull(functor);
    return (T t) -> {
      accept(functor.apply(t));
    };
  }

  default Acceptor<T> filter(Filter<? super T> filter) {
    Objects.requireNonNull(filter);
    return (T t) -> {
      if (filter.test(t)) {
        accept(t);
      }
    };
  }
}
