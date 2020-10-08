package com.base.seed.common.monad.primitive;

@FunctionalInterface
public interface BoolFunctor<T>  {

    T apply(boolean value);

    default T apply(Boolean value) {
        return apply(value.booleanValue());
    }
}

