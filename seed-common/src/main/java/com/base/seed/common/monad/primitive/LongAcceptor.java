package com.base.seed.common.monad.primitive;

import com.base.seed.common.monad.Acceptor;
import java.util.function.LongConsumer;

@FunctionalInterface
public interface LongAcceptor extends Acceptor<Long>, LongConsumer {

    @Override
    default void accept(Long i) {
        accept(i.longValue());
    }

}