package com.base.seed.common.monad.bi;

import com.base.seed.common.monad.Acceptor;
import com.base.seed.common.monad.Filter;
import com.base.seed.common.monad.Functor;
import java.util.Objects;
import java.util.function.BiFunction;

@FunctionalInterface
public interface BiFunctor<T, U, R> extends BiFunction<T, U, R> {

    default <V> BiFunctor<T, U, V> map(Functor<? super R, ? extends V> functor) {
        Objects.requireNonNull(functor);
        return (t, u) -> functor.apply(apply(t, u));
    }

    default BiFilter<T, U> filter(Filter<? super R> filter) {
        Objects.requireNonNull(filter);
        return (t, u) -> filter.test(apply(t, u));
    }

    default BiAcceptor<T, U> sink(Acceptor<? super R> acceptor) {
        Objects.requireNonNull(acceptor);
        return (t, u) -> acceptor.accept(apply(t, u));
    }

    static <T, U, R> R toNull(T t, U u) {
        return null;
    }
}
