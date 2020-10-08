package com.base.seed.common.monad.primitive.pure;

import com.base.seed.common.monad.Filter;

public interface CompareInt<T> extends CompareLong<T> {

    int applyAsInt(T t);

    @Override
    default long applyAsLong(T t) {
        return applyAsInt(t);
    }

    default Filter<T> eq(int v) {
        return t -> applyAsInt(t) == v;
    }
    default Filter<T> gt(int v) {
        return t -> applyAsInt(t) > v;
    }
    default Filter<T> ge(int v) {
        return t -> applyAsInt(t) >= v;
    }
    default Filter<T> lt(int v) {
        return t -> applyAsInt(t) < v;
    }
    default Filter<T> le(int v) {
        return t -> applyAsInt(t) <= v;
    }

}
