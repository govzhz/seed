package com.base.seed.common.monad;

import com.base.seed.common.monad.bi.BiBoolOp;
import com.base.seed.common.monad.primitive.BoolFunctor;
import com.base.seed.common.monad.primitive.DoubleFilter;
import com.base.seed.common.monad.primitive.IntFilter;
import com.base.seed.common.monad.primitive.LongFilter;
import com.base.seed.common.monad.primitive.ToDoubleFunctor;
import com.base.seed.common.monad.primitive.ToIntFunctor;
import com.base.seed.common.monad.primitive.ToLongFunctor;
import java.util.Comparator;
import java.util.Objects;


@FunctionalInterface
public interface Filter<T> extends java.util.function.Predicate<T> {

    default Boolean apply(T t) {
        return test(t);
    }

    default Filter<T> not() { return t -> !test(t); }

    default Joiner<T> and() { return new Joiner<>(this, BiBoolOp.AND); }

    default Joiner<T> or() { return new Joiner<>(this, BiBoolOp.OR); }

    default <R> Functor<T, R> map(BoolFunctor<? extends R> functor) {
        Objects.requireNonNull(functor);
        return t -> functor.apply(test(t));
    }

    default <R> Functor<T, R> map(
            Functor<? super T, ? extends R> ifTrue,
            Functor<? super T, ? extends R> ifFalse) {
        Objects.requireNonNull(ifTrue);
        if (ifFalse == null) return map(ifTrue, Functor::toNull);
        return t -> test(t)? ifTrue.apply(t): ifFalse.apply(t);
    }

    default ToIntFunctor<T> mapToInt(ToIntFunctor<? super T> ifTrue, int ifFalse) {
        Objects.requireNonNull(ifTrue);
        return t -> test(t)? ifTrue.apply(t): ifFalse;
    }

    default ToLongFunctor<T> mapToLong(ToLongFunctor<? super T> ifTrue, long ifFalse) {
        Objects.requireNonNull(ifTrue);
        return t -> test(t)? ifTrue.apply(t): ifFalse;
    }

    default ToDoubleFunctor<T> mapToDouble(ToDoubleFunctor<? super T> ifTrue, double ifFalse) {
        Objects.requireNonNull(ifTrue);
        return t -> test(t)? ifTrue.apply(t): ifFalse;
    }

    default Acceptor<T> sink(
        Acceptor<? super T> acceptor) {
        return t -> { if (test(t)) acceptor.accept(t); };
    }

    default Acceptor<T> sink(
        Acceptor<? super T> ifTrue,
        Acceptor<? super T> ifFalse) {
        return t -> {
            Acceptor<? super T> a = test(t)? ifTrue: ifFalse;
            if (a != null) a.accept(t);
        };
    }

    default <R> java.util.function.Function<T, R> get(
        Producer<? extends R> ifTrue,
        Producer<? extends R> ifFalse) {
        return t -> {
            Producer<? extends R> p = test(t)? ifTrue: ifFalse;
            return p == null? null: p.get();
        };
    }

    @Override
    default Filter<T> and(java.util.function.Predicate<? super T> other) {
        Objects.requireNonNull(other);
        return (t) -> test(t) && other.test(t);
    }

    @Override
    default Filter<T> or(java.util.function.Predicate<? super T> other) {
        Objects.requireNonNull(other);
        return (t) -> test(t) || other.test(t);
    }

    static <T> Filter<T> notNull(Class<T> clazz) { return Objects::nonNull;  }
    static <T> Filter<T> isNull(Class<T> clazz) { return Objects::isNull; }

    static <T,R> Filter<T> notNull(java.util.function.Function<T,R> function) { return t -> function.apply(t) != null;  }
    static <T,R> Filter<T> isNull(java.util.function.Function<T,R> function) { return t -> function.apply(t) == null; }

    static IntFilter eqNum(int v) { return t -> t == v; }
    static IntFilter gtNum(int v) { return t -> t > v; }
    static IntFilter geNum(int v) { return t -> t >= v; }
    static IntFilter ltNum(int v) { return t -> t < v; }
    static IntFilter leNum(int v) { return t -> t <= v; }
    static LongFilter eqNum(long v) { return t -> t == v; }
    static LongFilter gtNum(long v) { return t -> t > v; }
    static LongFilter geNum(long v) { return t -> t >= v; }
    static LongFilter ltNum(long v) { return t -> t < v; }
    static LongFilter leNum(long v) { return t -> t <= v; }
    static DoubleFilter eqNum(double v) { return t -> t == v; }
    static DoubleFilter gtNum(double v) { return t -> t > v; }
    static DoubleFilter geNum(double v) { return t -> t >= v; }
    static DoubleFilter ltNum(double v) { return t -> t < v; }
    static DoubleFilter leNum(double v) { return t -> t <= v; }

    static <T> Filter<T> is(T v) {return t -> t == v || t.equals(v); }
    static <T> Filter<T> eq(T v) {return t -> ((Comparable) t).compareTo(v) == 0; }
    static <T> Filter<T> gt(T v) {return t -> ((Comparable) t).compareTo(v) > 0; }
    static <T> Filter<T> ge(T v) { return t -> ((Comparable) t).compareTo(v) >= 0; }
    static <T> Filter<T> lt(T v) { return t -> ((Comparable) t).compareTo(v) < 0; }
    static <T> Filter<T> le(T v) { return t -> ((Comparable) t).compareTo(v) <= 0; }
    static <T> Filter<T> eq(T v, Comparator<? super T> comparator) { return t -> comparator.compare(t, v) == 0; }
    static <T> Filter<T> gt(T v, Comparator<? super T> comparator) {
        return t -> comparator.compare(t, v) > 0;
    }
    static <T> Filter<T> ge(T v, Comparator<? super T> comparator) { return t -> comparator.compare(t, v) >= 0; }
    static <T> Filter<T> lt(T v, Comparator<? super T> comparator) {
        return t -> comparator.compare(t, v) < 0;
    }
    static <T> Filter<T> le(T v, Comparator<? super T> comparator) { return t -> comparator.compare(t, v) <= 0; }
}
