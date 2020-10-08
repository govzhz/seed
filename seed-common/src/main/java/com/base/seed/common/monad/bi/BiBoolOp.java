package com.base.seed.common.monad.bi;

import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;

public interface BiBoolOp {

  <T> java.util.function.Predicate<T> test(
      java.util.function.Predicate<? super T> a, java.util.function.Predicate<? super T> b);

  IntPredicate test(IntPredicate a, IntPredicate b);
  LongPredicate test(LongPredicate a, LongPredicate b);
  DoublePredicate test(DoublePredicate a, DoublePredicate b);

  default <T> java.util.function.Predicate<T> of(
      java.util.function.Predicate<? super T> a, java.util.function.Predicate<? super T> b) {
    return test(a, b);
  }

  default IntPredicate of(IntPredicate a, IntPredicate b) {
    return test(a, b);
  }
  default LongPredicate of(LongPredicate a, LongPredicate b) {
    return test(a, b);
  }
  default DoublePredicate of(DoublePredicate a, DoublePredicate b) {
    return test(a, b);
  }

  BiBoolOp
      AND = new BiBoolOp() {
        public <T> java.util.function.Predicate<T> test(
            java.util.function.Predicate<? super T> a, java.util.function.Predicate<? super T> b) {
          return t -> a.test(t) && b.test(t);
        }
        public IntPredicate test(IntPredicate a, IntPredicate b) {
          return t -> a.test(t) && b.test(t);
        }
        public LongPredicate test(LongPredicate a, LongPredicate b) {
          return t -> a.test(t) && b.test(t);
        }
        public DoublePredicate test(DoublePredicate a, DoublePredicate b) {
          return t -> a.test(t) && b.test(t);
        }
      },
      OR = new BiBoolOp() {
        public <T> java.util.function.Predicate<T> test(
            java.util.function.Predicate<? super T> a, java.util.function.Predicate<? super T> b) {
          return t -> a.test(t) || b.test(t);
        }
        public IntPredicate test(IntPredicate a, IntPredicate b) {
          return t -> a.test(t) || b.test(t);
        }
        public LongPredicate test(LongPredicate a, LongPredicate b) {
          return t -> a.test(t) || b.test(t);
        }
        public DoublePredicate test(DoublePredicate a, DoublePredicate b) {
          return t -> a.test(t) || b.test(t);
        }
      };
}
