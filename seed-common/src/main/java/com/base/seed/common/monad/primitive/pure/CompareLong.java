package com.base.seed.common.monad.primitive.pure;

import com.base.seed.common.monad.Filter;

public interface CompareLong<T> extends CompareDouble<T> {

    long applyAsLong(T t);

    @Override
    default double applyAsDouble(T t) {
        return applyAsLong(t);
    }

    default Filter<T> eq(long v) {
        return t -> applyAsLong(t) == v;
    }
    default Filter<T> gt(long v) {
        return t -> applyAsLong(t) > v;
    }
    default Filter<T> ge(long v) {
        return t -> applyAsLong(t) >= v;
    }
    default Filter<T> lt(long v) {
        return t -> applyAsLong(t) < v;
    }
    default Filter<T> le(long v) {
        return t -> applyAsLong(t) <= v;
    }

}
