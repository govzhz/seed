package com.base.seed.common.number;

import java.math.BigDecimal;
import javax.annotation.Nullable;

public class BigDecimalUtil {

  /**
   * 判断两个 BigDecimal 是否相等，允许参数为空
   */
  public static boolean isEqual(@Nullable BigDecimal a, @Nullable BigDecimal b) {
    return a == b || (a != null && a.compareTo(b) == 0);
  }
}
