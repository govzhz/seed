package com.base.seed.common.monad.primitive;

import java.util.function.DoubleFunction;

@FunctionalInterface
public interface DoubleFunctor<T> extends DoubleFunction<T> {

    default T apply(Double v) {
        return apply(v.doubleValue());
    }
}

