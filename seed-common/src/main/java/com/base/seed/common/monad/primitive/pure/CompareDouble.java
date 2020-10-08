package com.base.seed.common.monad.primitive.pure;

import com.base.seed.common.monad.Filter;

public interface CompareDouble<T>  {

    double applyAsDouble(T t);

    default Filter<T> eq(double v) {
        return t -> applyAsDouble(t) == v;
    }
    default Filter<T> gt(double v) {
        return t -> applyAsDouble(t) > v;
    }
    default Filter<T> ge(double v) {
        return t -> applyAsDouble(t) >= v;
    }
    default Filter<T> lt(double v) {
        return t -> applyAsDouble(t) < v;
    }
    default Filter<T> le(double v) {
        return t -> applyAsDouble(t) <= v;
    }
}
