package com.base.seed.common.monad.primitive;

import com.base.seed.common.monad.Acceptor;

@FunctionalInterface
public interface BoolAcceptor extends Acceptor<Boolean> {

    void accept(boolean t);

    @Override
    default void accept(Boolean b) {
        accept(b.booleanValue());

    }
}