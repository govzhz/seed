package com.base.seed.common.monad;

import com.base.seed.common.monad.primitive.BoolProducer;
import java.util.Objects;

@FunctionalInterface
public interface Producer<T> extends java.util.function.Supplier<T> {

    default Producer<? extends T> orGet(T v) {
        return map(Functor.ifNull(v));
    }

    default <Y> Producer<? extends Y> map(Functor<? super T, ? extends Y> after) {
        Objects.requireNonNull(after);
        return () -> after.apply(get());
    }

    default BoolProducer filter(Filter<? super T> filter) {
        return () -> filter.test(get());
    }
}
