package com.base.seed.common.monad.primitive;

import com.base.seed.common.monad.Joiner;
import com.base.seed.common.monad.bi.BiBoolOp;
import com.base.seed.common.monad.primitive.pure.LongToLongFunctor;
import java.util.function.LongPredicate;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;

/**
 * <p> Created by pengshuolin on 2018/8/11
 */
public class LongJoiner<T> extends Joiner<T> {

  final ToLongFunctor<T> mapper;
  final LongPredicate chain;

  public LongJoiner(ToLongFunctor<T> mapper, java.util.function.Predicate<? super T> filter, BiBoolOp joiner) {
    this(mapper, filter, joiner, null);
  }

  LongJoiner(ToLongFunctor<T> mapper, java.util.function.Predicate<? super T> filter, BiBoolOp joiner, LongPredicate chain) {
    super(filter, joiner);
    this.mapper = mapper;
    this.chain = chain;
  }

  @Override
  public boolean test(T t) {
    if (joiner == BiBoolOp.AND) {
      return (filter == null || filter.test(t)) && (chain == null || chain.test(mapper.applyAsLong(t)));
    }
    if (joiner == BiBoolOp.OR) {
      return (filter == null || filter.test(t)) || (chain == null || chain.test(mapper.applyAsLong(t)));
    }
    throw new AssertionError();
  }

  @Override
  public LongJoiner<T> and() {
    if (joiner == BiBoolOp.AND) return this;
    return new LongJoiner<>(mapper, this, BiBoolOp.AND);
  }

  @Override
  public LongJoiner<T> or() {
    if (joiner == BiBoolOp.OR) return this;
    return new LongJoiner<>(mapper, this, BiBoolOp.OR);
  }

  public LongJoiner<T> when(LongPredicate predicate) {
    LongPredicate join = chain == null? predicate: joiner.of(chain, predicate);
    return new LongJoiner<>(mapper, filter, joiner, join);
  }

  public <R> MaJoiner<T, R> join(java.util.function.LongFunction<? extends R> function) {
    java.util.function.Function<T,R> mapper = t -> function.apply(this.mapper.applyAsLong(t));
    return new MaJoiner<>(mapper, this, joiner);
  }

  public <R> java.util.function.Function<T, R> to(java.util.function.LongFunction<? extends R> ifTrue) {
    return to(ifTrue, null);
  }

  public <R> java.util.function.Function<T, R> to(
      java.util.function.LongFunction<? extends R> ifTrue, java.util.function.LongFunction<? extends R> ifFalse) {
    return t -> {
      java.util.function.LongFunction<? extends R> f = test(t)? ifTrue: ifFalse;
      return f == null? null: f.apply(mapper.applyAsLong(t));
    };
  }

  public IntJoiner<T> joinInt(LongToIntFunction function) {
    ToIntFunctor<T> toInt = t -> function.applyAsInt(this.mapper.applyAsLong(t));
    return new IntJoiner<>(toInt, this, joiner);
  }

  public LongJoiner<T> joinLong(LongToLongFunctor function) {
    ToLongFunctor<T> toLong = t -> function.applyAsLong(this.mapper.applyAsLong(t));
    return new LongJoiner<>(toLong, this, joiner);
  }

  public DoubleJoiner<T> joinDouble(LongToDoubleFunction function) {
    ToDoubleFunctor<T> toDouble = t -> function.applyAsDouble(this.mapper.applyAsLong(t));
    return new DoubleJoiner<>(toDouble, this, joiner);
  }

}