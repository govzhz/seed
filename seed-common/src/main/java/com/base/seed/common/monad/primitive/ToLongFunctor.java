package com.base.seed.common.monad.primitive;

import com.base.seed.common.monad.Filter;
import com.base.seed.common.monad.bi.BiBoolOp;
import com.base.seed.common.monad.primitive.pure.CompareLong;
import java.util.Objects;
import java.util.function.LongPredicate;

@FunctionalInterface
public interface ToLongFunctor<T> extends java.util.function.ToLongFunction<T>, CompareLong<T> {

    default Long apply(T t) {
      return applyAsLong(t);
    }

    default Filter<T> filter(LongPredicate after) {
        Objects.requireNonNull(after);
        return (T t) -> after.test(applyAsLong(t));
    }

    default <R> java.util.function.Function<T,R> map(java.util.function.LongFunction<? extends R> after) {
        Objects.requireNonNull(after);
        return t -> after.apply(this.applyAsLong(t));
    }

    default LongJoiner<T> and() {
        return new LongJoiner<>(this, null, BiBoolOp.AND);
    }

    default LongJoiner<T> or() {
        return new LongJoiner<>(this, null, BiBoolOp.OR);
    }
}
