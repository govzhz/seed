package com.base.seed.common.monad.primitive;

import com.base.seed.common.monad.Filter;
import com.base.seed.common.monad.Functor;
import com.base.seed.common.monad.primitive.pure.LongToLongFunctor;
import java.util.Objects;
import java.util.function.LongPredicate;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;

@FunctionalInterface
public interface LongFilter extends LongPredicate, Filter<Long> {

    @Override
    default boolean test(Long x) {
        return test(x.longValue());
    }

    @Override
    default Boolean apply(Long x) {
        return test(x);
    }

    default LongFilter and(LongFilter other) {
        Objects.requireNonNull(other);
        return (long value) -> test(value) && other.test(value);
    }

    default LongFilter or(LongFilter other) {
        Objects.requireNonNull(other);
        return (long value) -> test(value) && other.test(value);
    }

    @Override
    default LongFilter negate() {
        return (value) -> !test(value);
    }

    default <R> LongFunctor<R> map(
        LongFunctor<? extends R> ifTrue,
        LongFunctor<? extends R> ifFalse) {
        Objects.requireNonNull(ifTrue);
        if (ifFalse == null) return map(ifTrue, Functor::toNull);
        return t -> test(t)? ifTrue.apply(t): ifFalse.apply(t);
    }

    default LongToIntFunction mapToInt(
            LongToIntFunction ifTrue, int ifFalse) {
        Objects.requireNonNull(ifTrue);
        return t -> test(t)? ifTrue.applyAsInt(t): ifFalse;
    }

    default LongToLongFunctor mapToLong(
            LongToLongFunctor ifTrue, long ifFalse) {
        Objects.requireNonNull(ifTrue);
        return t -> test(t)? ifTrue.applyAsLong(t): ifFalse;
    }

    default LongToDoubleFunction mapToDouble(
            LongToDoubleFunction ifTrue, double ifFalse) {
        Objects.requireNonNull(ifTrue);
        return t -> test(t)? ifTrue.applyAsDouble(t): ifFalse;
    }
}