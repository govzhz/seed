package com.base.seed.common.monad.bi;

import com.base.seed.common.monad.Acceptor;
import com.base.seed.common.monad.Filter;
import com.base.seed.common.monad.Functor;
import java.util.Objects;
import java.util.function.BiConsumer;

@FunctionalInterface
public interface BiAcceptor<T, U> extends BiConsumer<T, U> {

  default BiAcceptor<T, U> then(BiAcceptor<? super T, ? super U> after) {
    Objects.requireNonNull(after);
    return (t, u) -> {
      accept(t, u);
      after.accept(t, u);
    };
  }

  default BiAcceptor<T, U> thenLeft(Acceptor<? super T> after) {
    Objects.requireNonNull(after);
    return (t, u) -> {
      accept(t, u);
      after.accept(t);
    };
  }

  default BiAcceptor<T, U> thenRight(Acceptor<? super U> after) {
    Objects.requireNonNull(after);
    return (t, u) -> {
      accept(t, u);
      after.accept(u);
    };
  }

  default BiAcceptor<T, U> map(
      Functor<? super T, ? extends T> left,
      Functor<? super U, ? extends U> right) {
    if (left == null || right == null) {
      return map(
          left == null ? (Functor<T, T>) java.util.function.Function.identity() : left,
          right == null ? (Functor<U, U>) java.util.function.Function.identity() : right);
    }
    return (t, u) -> {
      accept(left.apply(t), right.apply(u));
    };
  }

  default BiAcceptor<T, U> mapLeft(Functor<? super T, ? extends T> functor) {
    Objects.requireNonNull(functor);
    return (t, u) -> {
      accept(functor.apply(t), u);
    };
  }

  default BiAcceptor<T, U> mapRight(Functor<? super U, ? extends U> functor) {
    Objects.requireNonNull(functor);
    return (t, u) -> {
      accept(t, functor.apply(u));
    };
  }

  default BiAcceptor<T, U> filter(BiFilter<? super T, ? super U> filter) {
    Objects.requireNonNull(filter);
    return (t, u) -> {
      if (filter.test(t, u)) {
        accept(t, u);
      }
    };
  }

  default BiAcceptor<T, U> filterLeft(Filter<? super T> filter) {
    Objects.requireNonNull(filter);
    return (t, u) -> {
      if (filter.test(t)) {
        accept(t, u);
      }
    };
  }

  default BiAcceptor<T, U> filterRight(Filter<? super U> filter) {
    Objects.requireNonNull(filter);
    return (t, u) -> {
      if (filter.test(u)) {
        accept(t, u);
      }
    };
  }
}
