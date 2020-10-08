package com.base.seed.common.monad.primitive;

import com.base.seed.common.monad.Joiner;
import com.base.seed.common.monad.bi.BiBoolOp;
import com.base.seed.common.monad.primitive.pure.DoubleToDoubleFunctor;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;

/**
 * <p> Created by pengshuolin on 2018/8/11
 */
public class DoubleJoiner<T> extends Joiner<T> {

  final ToDoubleFunctor<T> mapper;
  final DoublePredicate chain;

  public DoubleJoiner(ToDoubleFunctor<T> mapper, java.util.function.Predicate<? super T> filter, BiBoolOp joiner) {
    this(mapper, filter, joiner, null);
  }

  DoubleJoiner(ToDoubleFunctor<T> mapper, java.util.function.Predicate<? super T> filter, BiBoolOp joiner, DoublePredicate chain) {
    super(filter, joiner);
    this.mapper = mapper;
    this.chain = chain;
  }

  @Override
  public boolean test(T t) {
    if (joiner == BiBoolOp.AND) {
      return (filter == null || filter.test(t)) && (chain == null || chain.test(mapper.applyAsDouble(t)));
    }
    if (joiner == BiBoolOp.OR) {
      return (filter == null || filter.test(t)) || (chain == null || chain.test(mapper.applyAsDouble(t)));
    }
    throw new AssertionError();
  }

  @Override
  public DoubleJoiner<T> and() {
    if (joiner == BiBoolOp.AND) return this;
    return new DoubleJoiner<>(mapper, this, BiBoolOp.AND);
  }

  @Override
  public DoubleJoiner<T> or() {
    if (joiner == BiBoolOp.OR) return this;
    return new DoubleJoiner<>(mapper, this, BiBoolOp.OR);
  }

  public DoubleJoiner<T> when(DoublePredicate predicate) {
    DoublePredicate join = chain == null? predicate: joiner.of(chain, predicate);
    return new DoubleJoiner<>(mapper, filter, joiner, join);
  }

  public <R> MaJoiner<T, R> join(DoubleFunction<? extends R> function) {
    java.util.function.Function<T,R> mapper = t -> function.apply(this.mapper.applyAsDouble(t));
    return new MaJoiner<>(mapper, this, joiner);
  }

  public <R> java.util.function.Function<T, R> to(DoubleFunction<? extends R> ifTrue) {
    return to(ifTrue, null);
  }

  public <R> java.util.function.Function<T, R> to(
      DoubleFunction<? extends R> ifTrue, DoubleFunction<? extends R> ifFalse) {
    return t -> {
      DoubleFunction<? extends R> f = test(t)? ifTrue: ifFalse;
      return f == null? null: f.apply(mapper.applyAsDouble(t));
    };
  }

  public IntJoiner<T> joinInt(DoubleToIntFunction function) {
    ToIntFunctor<T> toInt = t -> function.applyAsInt(this.mapper.applyAsDouble(t));
    return new IntJoiner<>(toInt, this, joiner);
  }

  public LongJoiner<T> joinLong(DoubleToLongFunction function) {
    ToLongFunctor<T> toLong = t -> function.applyAsLong(this.mapper.applyAsDouble(t));
    return new LongJoiner<>(toLong, this, joiner);
  }

  public DoubleJoiner<T> joinDouble(DoubleToDoubleFunctor function) {
    ToDoubleFunctor<T> toDouble = t -> function.applyAsDouble(this.mapper.applyAsDouble(t));
    return new DoubleJoiner<>(toDouble, this, joiner);
  }

}
