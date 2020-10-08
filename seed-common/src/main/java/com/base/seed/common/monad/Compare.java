package com.base.seed.common.monad;

import java.util.Comparator;

interface Compare<T, R> extends java.util.function.Function<T, R> {

    default Filter<T> is(R v) {
        return t -> {
            R r = apply(t);
            return r == v || t.equals(r);
        };
    }

    default Filter<T> eq(R v) {
        return t -> ((Comparable) apply(t)).compareTo(v) == 0;
    }
    default Filter<T> gt(R v) {
        return t -> ((Comparable) apply(t)).compareTo(v) > 0;
    }
    default Filter<T> ge(R v) {
        return t -> ((Comparable) apply(t)).compareTo(v) >= 0;
    }
    default Filter<T> lt(R v) {
        return t -> ((Comparable) apply(t)).compareTo(v) < 0;
    }
    default Filter<T> le(R v) {
        return t -> ((Comparable) apply(t)).compareTo(v) <= 0;
    }

    default Filter<T> eq(R v, Comparator<? super R> comparator) {
        return t -> comparator.compare(apply(t), v) == 0;
    }
    default Filter<T> gt(R v, Comparator<? super R> comparator) {
        return t -> comparator.compare(apply(t), v) > 0;
    }
    default Filter<T> ge(R v, Comparator<? super R> comparator) {
        return t -> comparator.compare(apply(t), v) >= 0;
    }
    default Filter<T> lt(R v, Comparator<? super R> comparator) {
        return t -> comparator.compare(apply(t), v) < 0;
    }
    default Filter<T> le(R v, Comparator<? super R> comparator) {
        return t -> comparator.compare(apply(t), v) <= 0;
    }

}
