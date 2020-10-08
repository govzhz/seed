package com.base.seed.common.monad;

import com.base.seed.common.monad.bi.BiBoolOp;
import com.base.seed.common.monad.primitive.ToDoubleFunctor;
import com.base.seed.common.monad.primitive.ToIntFunctor;
import com.base.seed.common.monad.primitive.ToLongFunctor;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;


@SuppressWarnings("all")
@FunctionalInterface
public interface Functor<X, Y> extends Function<X,Y>, Compare<X,Y> {

    default Joiner.MaJoiner<X,Y> and() { return new Joiner.MaJoiner(this, null, BiBoolOp.AND); }

    default Joiner.MaJoiner<X,Y> or() { return new Joiner.MaJoiner(this, null, BiBoolOp.OR); }

    default <V> Functor<X, V> map(Function<? super Y, ? extends V> after) {
        Objects.requireNonNull(after);
        return x -> after.apply(apply(x));
    }

    default Filter<X> filter(Predicate<? super Y> after) {
        Objects.requireNonNull(after);
        return x -> after.test(apply(x));
    }

    default Joiner.MaJoiner<X, Y> when(Predicate<? super Y> after) {
        Objects.requireNonNull(after);
        Predicate<X> filter = x -> after.test(apply(x));
        return new Joiner.MaJoiner<>(this, filter, BiBoolOp.AND);
    }

    default Acceptor<X> sink(Consumer<? super Y> after) {
        Objects.requireNonNull(after);
        return x -> after.accept(apply(x));
    }

    default Acceptor<X> sinkInt(IntConsumer after) {
        Objects.requireNonNull(after);
        if (this instanceof ToIntFunctor) {
            ToIntFunctor<? super X> self = (ToIntFunctor<? super X>) this;
            return x -> after.accept(self.applyAsInt(x));
        }
        Functor<X, Integer> self = (Functor<X, Integer>) this;
        return x -> after.accept(self.apply(x));
    }

    default Acceptor<X> sinkLong(LongConsumer after) {
        Objects.requireNonNull(after);
        if (this instanceof ToLongFunctor) {
            ToLongFunctor<? super X> self = (ToLongFunctor<? super X>) this;
            return x -> after.accept(self.applyAsLong(x));
        }
        Functor<X, Long> self = (Functor<X, Long>) this;
        return x -> after.accept(self.apply(x));
    }

    default Acceptor<X> sinkDouble(DoubleConsumer after) {
        Objects.requireNonNull(after);
        if (this instanceof ToDoubleFunctor) {
            ToDoubleFunctor<? super X> self = (ToDoubleFunctor<? super X>) this;
            return x -> after.accept(self.applyAsDouble(x));
        }
        Functor<X, Double> self = (Functor<X, Double>) this;
        return x -> after.accept(self.apply(x));
    }

    default ToIntFunctor<X> mapToInt(ToIntFunction<? super Y> after) {
        Objects.requireNonNull(after);
        return x -> after.applyAsInt(apply(x));
    }

    default ToLongFunctor<X> mapToLong(ToLongFunction<? super Y> after) {
        Objects.requireNonNull(after);
        return x -> after.applyAsLong(apply(x));
    }

    default ToDoubleFunctor<X> mapToDouble(ToDoubleFunction<? super Y> after) {
        Objects.requireNonNull(after);
        return x -> after.applyAsDouble(apply(x));
    }

    static <T> Functor<T,T> ifNull(T t) {
        return x -> x == null? t: x;
    }

    static <T,R> R toNull(T o) {
        return null;
    }

}

