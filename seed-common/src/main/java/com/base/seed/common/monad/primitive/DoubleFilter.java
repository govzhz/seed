package com.base.seed.common.monad.primitive;

import com.base.seed.common.monad.Filter;
import com.base.seed.common.monad.Functor;
import com.base.seed.common.monad.primitive.pure.DoubleToDoubleFunctor;
import java.util.Objects;
import java.util.function.DoublePredicate;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;

@FunctionalInterface
public interface DoubleFilter extends DoublePredicate, Filter<Double> {

    @Override
    default boolean test(Double x) {
        return test(x.doubleValue());
    }

    @Override
    default Boolean apply(Double x) {
        return test(x);
    }

    default DoubleFilter and(DoubleFilter other) {
        Objects.requireNonNull(other);
        return (double value) -> test(value) && other.test(value);
    }

    default DoubleFilter or(DoubleFilter other) {
        Objects.requireNonNull(other);
        return (double value) -> test(value) && other.test(value);
    }

    @Override
    default DoubleFilter negate() {
        return (value) -> !test(value);
    }

    default <R> DoubleFunctor<R> map(
        DoubleFunctor<? extends R> ifTrue,
        DoubleFunctor<? extends R> ifFalse) {
        Objects.requireNonNull(ifTrue);
        if (ifFalse == null) return map(ifTrue, Functor::toNull);
        return t -> test(t)? ifTrue.apply(t): ifFalse.apply(t);
    }

    default DoubleToIntFunction mapToInt(
            DoubleToIntFunction ifTrue, int ifFalse) {
        Objects.requireNonNull(ifTrue);
        return t -> test(t)? ifTrue.applyAsInt(t): ifFalse;
    }

    default DoubleToLongFunction mapToLong(
            DoubleToLongFunction ifTrue, long ifFalse) {
        Objects.requireNonNull(ifTrue);
        return t -> test(t)? ifTrue.applyAsLong(t): ifFalse;
    }

    default DoubleToDoubleFunctor mapToDouble(
            DoubleToDoubleFunctor ifTrue, double ifFalse) {
        Objects.requireNonNull(ifTrue);
        return t -> test(t)? ifTrue.applyAsDouble(t): ifFalse;
    }
}