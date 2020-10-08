package com.base.seed.common.monad.primitive;

import com.base.seed.common.monad.Acceptor;
import java.util.function.IntConsumer;

@FunctionalInterface
public interface IntAcceptor extends Acceptor<Integer>, IntConsumer {

    @Override
    default void accept(Integer i) {
        accept(i.intValue());
    }

}