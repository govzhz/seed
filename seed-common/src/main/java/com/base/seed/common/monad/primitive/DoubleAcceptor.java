package com.base.seed.common.monad.primitive;

import com.base.seed.common.monad.Acceptor;
import java.util.function.DoubleConsumer;

@FunctionalInterface
public interface DoubleAcceptor extends Acceptor<Double>, DoubleConsumer {

    @Override
    default void accept(Double d) {
        accept(d.doubleValue());
    }

}