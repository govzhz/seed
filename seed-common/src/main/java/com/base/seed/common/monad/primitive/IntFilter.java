package com.base.seed.common.monad.primitive;

import com.base.seed.common.monad.Filter;
import com.base.seed.common.monad.Functor;
import com.base.seed.common.monad.primitive.pure.IntToIntFunctor;
import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;


@FunctionalInterface
public interface IntFilter extends IntPredicate, Filter<Integer> {

    @Override
    default boolean test(Integer x) {
        return test(x.intValue());
    }

    @Override
    default Boolean apply(Integer x) {
        return test(x);
    }

    default IntFilter and(IntFilter other) {
        Objects.requireNonNull(other);
        return (int value) -> test(value) && other.test(value);
    }

    default IntFilter or(IntFilter other) {
        Objects.requireNonNull(other);
        return (int value) -> test(value) && other.test(value);
    }

    @Override
    default IntFilter negate() {
        return (value) -> !test(value);
    }

    default <R> IntFunctor<R> map(
        IntFunctor<? extends R> ifTrue,
        IntFunctor<? extends R> ifFalse) {
        Objects.requireNonNull(ifTrue);
        if (ifFalse == null) return map(ifTrue, Functor::toNull);
        return t -> test(t)? ifTrue.apply(t): ifFalse.apply(t);
    }

    default IntToIntFunctor mapToInt(
            IntToIntFunctor ifTrue, int ifFalse) {
        Objects.requireNonNull(ifTrue);
        return t -> test(t)? ifTrue.applyAsInt(t): ifFalse;
    }

    default IntToLongFunction mapToLong(
            IntToLongFunction ifTrue, long ifFalse) {
        Objects.requireNonNull(ifTrue);
        return t -> test(t)? ifTrue.applyAsLong(t): ifFalse;
    }

    default IntToDoubleFunction mapToDouble(
            IntToDoubleFunction ifTrue, double ifFalse) {
        Objects.requireNonNull(ifTrue);
        return t -> test(t)? ifTrue.applyAsDouble(t): ifFalse;
    }
}