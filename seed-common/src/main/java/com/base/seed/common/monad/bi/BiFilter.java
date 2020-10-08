package com.base.seed.common.monad.bi;

import java.util.Objects;
import java.util.function.BiPredicate;

@FunctionalInterface
public interface BiFilter<T, U> extends BiPredicate<T, U> {

    default Boolean apply(T t, U u) {
        return test(t, u);
    }

    default BiFilter<T, U> not() { return (t, u) -> !test(t, u); }

    default <R> BiFunctor<T, U, R> map(
            BiFunctor<? super T, ? super U, ? extends R> ifTrue,
            BiFunctor<? super T, ? super U, ? extends R> ifFalse) {
        Objects.requireNonNull(ifTrue);
        if (ifFalse == null) return map(ifTrue, BiFunctor::toNull);
        return (t, u) -> test(t, u)? ifTrue.apply(t, u): ifFalse.apply(t, u);
    }

    default BiAcceptor<T, U> sink(BiAcceptor<? super T, ? super U> acceptor) {
        Objects.requireNonNull(acceptor);
        return (t, u) -> {
            if (test(t, u)) acceptor.accept(t, u);
        };
    }

    @Override
    default BiFilter<T, U> and(BiPredicate<? super T, ? super U> other) {
        Objects.requireNonNull(other);
        return (t, u) -> test(t, u) && other.test(t, u);
    }

    @Override
    default BiFilter<T, U> or(BiPredicate<? super T, ? super U> other) {
        Objects.requireNonNull(other);
        return (t, u) -> test(t, u) || other.test(t, u);
    }

    default BiFilter<T, U> andLeft(java.util.function.Predicate<? super T> left) {
        Objects.requireNonNull(left);
        return (t, u) -> test(t, u) && left.test(t);
    }

    default BiFilter<T, U> orLeft(java.util.function.Predicate<? super T> left) {
        Objects.requireNonNull(left);
        return (t, u) -> test(t, u) || left.test(t);
    }

    default BiFilter<T, U> andRight(java.util.function.Predicate<? super U> right) {
        Objects.requireNonNull(right);
        return (t, u) -> test(t, u) && right.test(u);
    }

    default BiFilter<T, U> orRight(java.util.function.Predicate<? super U> right) {
        Objects.requireNonNull(right);
        return (t, u) -> test(t, u) || right.test(u);
    }

}
