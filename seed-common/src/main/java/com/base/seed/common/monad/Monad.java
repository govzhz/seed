package com.base.seed.common.monad;

import com.base.seed.common.monad.bi.BiAcceptor;
import com.base.seed.common.monad.bi.BiBoolOp;
import com.base.seed.common.monad.bi.BiFilter;
import com.base.seed.common.monad.bi.BiFunctor;
import com.base.seed.common.monad.primitive.BoolAcceptor;
import com.base.seed.common.monad.primitive.BoolProducer;
import com.base.seed.common.monad.primitive.DoubleAcceptor;
import com.base.seed.common.monad.primitive.DoubleFilter;
import com.base.seed.common.monad.primitive.DoubleFunctor;
import com.base.seed.common.monad.primitive.DoubleProducer;
import com.base.seed.common.monad.primitive.IntAcceptor;
import com.base.seed.common.monad.primitive.IntFilter;
import com.base.seed.common.monad.primitive.IntFunctor;
import com.base.seed.common.monad.primitive.IntProducer;
import com.base.seed.common.monad.primitive.LongAcceptor;
import com.base.seed.common.monad.primitive.LongFilter;
import com.base.seed.common.monad.primitive.LongFunctor;
import com.base.seed.common.monad.primitive.LongProducer;
import com.base.seed.common.monad.primitive.ToDoubleFunctor;
import com.base.seed.common.monad.primitive.ToIntFunctor;
import com.base.seed.common.monad.primitive.ToLongFunctor;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleSupplier;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;


public final class Monad {

    private Monad() {}

    public static <T> Joiner<T> and(Class<T> clazz) { return new Joiner<>(null, BiBoolOp.AND); }
    public static <T> Joiner<T> or(Class<T> clazz) { return new Joiner<>(null, BiBoolOp.OR); }
    public static <X,Y> Joiner.MaJoiner<X,Y> and(Function<? super X, ? extends Y> function) { return new Joiner.MaJoiner<>(function, null, BiBoolOp.AND); }
    public static <X,Y> Joiner.MaJoiner<X,Y> or(Function<? super X, ? extends Y> function) { return new Joiner.MaJoiner<>(function, null, BiBoolOp.OR); }
    public static <T> Joiner<T> and(Predicate<? super T> predicate) { return new Joiner<>(predicate, BiBoolOp.AND); }
    public static <T> Joiner<T> or(Predicate<? super T> predicate) { return new Joiner<>(predicate, BiBoolOp.OR); }

    public static <X,Y> Functor<X,Y> map(Function<? super X, ? extends Y> function) {
        return function::apply;
    }
    public static <T> Filter<T> filter(Predicate<? super T> predicate) {
        return predicate::test;
    }
    public static <T> Acceptor<T> sink(Consumer<? super T> consumer) {
        return consumer::accept;
    }
    public static <T> Producer<T> of(Supplier<? extends T> supplier) {
        return supplier::get;
    }

    public static <X,Y,R> BiFunctor<X, Y, R> map(BiFunction<? super X, ? super Y, ? extends R> function) {
        return function::apply;
    }
    public static <T,U> BiFilter<T,U> filter(BiPredicate<? super T,? super U> predicate) {
        return predicate::test;
    }
    public static <T,U> BiAcceptor<T,U> sink(BiConsumer<? super T,? super U> consumer) {
        return consumer::accept;
    }


    public static IntFunctor mapInt(IntFunction function) {
        return function::apply;
    }
    public static LongFunctor mapLong(LongFunction function) {
        return function::apply;
    }
    public static DoubleFunctor mapDouble(DoubleFunction function) {
        return function::apply;
    }

    public static <T> ToIntFunctor<T> mapToInt(ToIntFunction<? super T> function) {
        return function::applyAsInt;
    }
    public static <T> ToLongFunctor<T> mapToLong(ToLongFunction<? super T> function) {
        return function::applyAsLong;
    }
    public static <T> ToDoubleFunctor<T> mapToDouble(ToDoubleFunction<? super T> function) {
        return function::applyAsDouble;
    }

    public static <T> Predicate<T> not(Predicate<T> predicate) {
        return t -> !predicate.test(t);
    }

    public static IntFilter filterInt(IntPredicate predicate) {
        return predicate::test;
    }
    public static LongFilter filterLong(LongPredicate predicate) {
        return predicate::test;
    }
    public static DoubleFilter filterDouble(DoublePredicate predicate) {
        return predicate::test;
    }

    public static BoolAcceptor sinkBool(BoolAcceptor consumer) {
        return consumer::accept;
    }
    public static IntAcceptor sinkInt(IntConsumer consumer) {
        return consumer::accept;
    }
    public static LongAcceptor sinkLong(LongConsumer consumer) {
        return consumer::accept;
    }
    public static DoubleAcceptor sinkDouble(DoubleConsumer consumer) {
        return consumer::accept;
    }

    public static BoolProducer ofBool(BooleanSupplier supplier) {
        return supplier::getAsBoolean;
    }
    public static IntProducer ofInt(IntSupplier supplier) {
        return supplier::getAsInt;
    }
    public static LongProducer ofLong(LongSupplier supplier) {
        return supplier::getAsLong;
    }
    public static DoubleProducer ofDouble(DoubleSupplier supplier) {
        return supplier::getAsDouble;
    }

}
