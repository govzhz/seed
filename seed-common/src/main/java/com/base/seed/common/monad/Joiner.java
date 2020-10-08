package com.base.seed.common.monad;

import com.base.seed.common.monad.bi.BiBoolOp;
import com.base.seed.common.monad.primitive.DoubleJoiner;
import com.base.seed.common.monad.primitive.IntJoiner;
import com.base.seed.common.monad.primitive.LongJoiner;
import com.base.seed.common.monad.primitive.ToDoubleFunctor;
import com.base.seed.common.monad.primitive.ToIntFunctor;
import com.base.seed.common.monad.primitive.ToLongFunctor;
import java.util.Objects;

public class Joiner<T> implements Filter<T> {

    // TODO_LIST_ENTITIES joiner
    public static class MaJoiner<T, X> extends Joiner<T> {

        final java.util.function.Function<? super T, ? extends X> mapper;
        final java.util.function.Predicate<? super X> chain;

        public MaJoiner(
                java.util.function.Function<? super T, ? extends X> mapper,
                java.util.function.Predicate<? super T> filter,
                BiBoolOp joiner) {
            this(mapper, filter, joiner, null);
        }

        private MaJoiner(
            java.util.function.Function<? super T, ? extends X> mapper,
            java.util.function.Predicate<? super T> filter,
            BiBoolOp joiner,
            java.util.function.Predicate<? super X> chain) {
            super(filter, joiner);
            this.mapper =  Objects.requireNonNull(mapper);
            this.chain = chain;
        }

        @Override
        public boolean test(T t) {
            if (joiner == BiBoolOp.AND) {
                return (filter == null || filter.test(t))
                    && (chain == null || chain.test(mapper.apply(t)));
            }
            if (joiner == BiBoolOp.OR) {
                return (filter == null || filter.test(t))
                    || (chain == null || chain.test(mapper.apply(t)));
            }
            throw new AssertionError();
        }

        @Override
        public MaJoiner<T, X> and() {
            if (joiner == BiBoolOp.AND) return this;
            return new MaJoiner<>(mapper, this, BiBoolOp.AND);
        }

        @Override
        public MaJoiner<T, X> or() {
            if (joiner == BiBoolOp.OR) return this;
            return new MaJoiner<>(mapper, this, BiBoolOp.OR);
        }

        public MaJoiner<T, X> when(java.util.function.Predicate<? super X> predicate) {
            java.util.function.Predicate<? super X> join = chain == null? predicate: joiner.of(chain, predicate);
            return new MaJoiner<>(mapper, filter, joiner, join);
        }

        public <Y> MaJoiner<T, Y> join(java.util.function.Function<? super X, ? extends Y> function) {
            return new MaJoiner<>(mapper.andThen(function), this, joiner);
        }

        public <Y> java.util.function.Function<T, Y> to(
            java.util.function.Function<? super X, ? extends Y> ifTrue) {
            return to(ifTrue, null);
        }

        public <Y> java.util.function.Function<T, Y> to(
            java.util.function.Function<? super X, ? extends Y> ifTrue,
            java.util.function.Function<? super X, ? extends Y> ifFalse) {
            return t -> {
                java.util.function.Function<? super X, ? extends Y> f = test(t)? ifTrue: ifFalse;
                return f == null? null: f.apply(mapper.apply(t));
            };
        }

        public IntJoiner<T> joinInt(java.util.function.ToIntFunction<? super X> function) {
            ToIntFunctor<T> toInt = t -> function.applyAsInt(mapper.apply(t));
            return new IntJoiner<>(toInt, this, joiner);
        }
        public LongJoiner<T> joinLong(java.util.function.ToLongFunction<? super X> function) {
            ToLongFunctor<T> toLong = t -> function.applyAsLong(mapper.apply(t));
            return new LongJoiner<>(toLong, this, joiner);
        }
        public DoubleJoiner<T> joinDouble(java.util.function.ToDoubleFunction<? super X> function) {
            ToDoubleFunctor<T> toDouble = t -> function.applyAsDouble(mapper.apply(t));
            return new DoubleJoiner<>(toDouble, this, joiner);
        }
    }

    protected final BiBoolOp joiner;
    protected final java.util.function.Predicate<? super T> filter;

    protected Joiner(java.util.function.Predicate<? super T> filter, BiBoolOp joiner) {
        if (joiner != BiBoolOp.AND && joiner != BiBoolOp.OR)
            throw new AssertionError();
        this.joiner = joiner;
        this.filter = filter;
    }

    @Override
    public boolean test(T t) {
        return filter == null || filter.test(t);
    }

    public <R> MaJoiner<T, R> map(java.util.function.Function<? super T, ? extends R> function) {
        return new MaJoiner<>(function, this, joiner);
    }

    public IntJoiner<T> mapToInt(java.util.function.ToIntFunction<? super T> function) {
        ToIntFunctor<T> toInt = function::applyAsInt;
        return new IntJoiner<>(toInt, this, joiner);
    }

    public LongJoiner<T> mapToLong(java.util.function.ToLongFunction<? super T> function) {
        ToLongFunctor<T> toLong = function::applyAsLong;
        return new LongJoiner<>(toLong, this, joiner);
    }

    public DoubleJoiner<T> mapToDouble(java.util.function.ToDoubleFunction<? super T> function) {
        ToDoubleFunctor<T> toDouble = function::applyAsDouble;
        return new DoubleJoiner<>(toDouble, this, joiner);
    }
}
