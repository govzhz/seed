package com.base.seed.common.monad.primitive;

import com.base.seed.common.monad.Joiner;
import com.base.seed.common.monad.bi.BiBoolOp;
import com.base.seed.common.monad.primitive.pure.IntToIntFunctor;
import java.util.function.IntPredicate;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;

public class IntJoiner<T> extends Joiner<T> {

  final ToIntFunctor<T> mapper;
  final IntPredicate chain;

  public IntJoiner(ToIntFunctor<T> mapper, java.util.function.Predicate<? super T> filter, BiBoolOp joiner) {
    this(mapper, filter, joiner, null);
  }

  private IntJoiner(ToIntFunctor<T> mapper, java.util.function.Predicate<? super T> filter, BiBoolOp joiner, IntPredicate chain) {
    super(filter, joiner);
    this.mapper = mapper;
    this.chain = chain;
  }

  @Override
  public boolean test(T t) {
    if (joiner == BiBoolOp.AND) {
      return (filter == null || filter.test(t)) && (chain == null || chain.test(mapper.applyAsInt(t)));
    }
    if (joiner == BiBoolOp.OR) {
      return (filter == null || filter.test(t)) || (chain == null || chain.test(mapper.applyAsInt(t)));
    }
    throw new AssertionError();
  }

  @Override
  public IntJoiner<T> and() {
    if (joiner == BiBoolOp.AND) return this;
    return new IntJoiner<>(mapper, this, BiBoolOp.AND);
  }

  @Override
  public IntJoiner<T> or() {
    if (joiner == BiBoolOp.OR) return this;
    return new IntJoiner<>(mapper, this, BiBoolOp.OR);
  }

  public IntJoiner<T> when(IntPredicate predicate) {
    IntPredicate join = chain == null? predicate: joiner.of(chain, predicate);
    return new IntJoiner<>(mapper, filter, joiner, join);
  }

  public <R> MaJoiner<T, R> join(java.util.function.IntFunction<? extends R> function) {
    java.util.function.Function<T,R> mapper = t -> function.apply(this.mapper.applyAsInt(t));
    return new MaJoiner<>(mapper, this, joiner);
  }

  public <R> java.util.function.Function<T, R> to(java.util.function.IntFunction<? extends R> ifTrue) {
    return to(ifTrue, null);
  }

  public <R> java.util.function.Function<T, R> to(
      java.util.function.IntFunction<? extends R> ifTrue, java.util.function.IntFunction<? extends R> ifFalse) {
    return t -> {
      java.util.function.IntFunction<? extends R> f = test(t)? ifTrue: ifFalse;
      return f == null? null: f.apply(mapper.applyAsInt(t));
    };
  }

  public IntJoiner<T> joinInt(IntToIntFunctor function) {
    ToIntFunctor<T> toInt = t -> function.applyAsInt(this.mapper.applyAsInt(t));
    return new IntJoiner<>(toInt, this, joiner);
  }

  public LongJoiner<T> joinLong(IntToLongFunction function) {
    ToLongFunctor<T> toLong = t -> function.applyAsLong(this.mapper.applyAsInt(t));
    return new LongJoiner<>(toLong, this, joiner);
  }

  public DoubleJoiner<T> joinDouble(IntToDoubleFunction function) {
    ToDoubleFunctor<T> toDouble = t -> function.applyAsDouble(this.mapper.applyAsInt(t));
    return new DoubleJoiner<>(toDouble, this, joiner);
  }
}
