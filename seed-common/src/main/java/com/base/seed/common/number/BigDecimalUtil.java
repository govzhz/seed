package com.base.seed.common.number;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Optional;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

public class BigDecimalUtil {

  /**
   * 判断两个 BigDecimal 是否相等，允许参数为空
   */
  public static boolean isEqual(@Nullable BigDecimal a, @Nullable BigDecimal b) {
    return a == b || (a != null && a.compareTo(b) == 0);
  }

  public static String toString(BigDecimal b) {
    return Optional.ofNullable(b)
        .map(bi -> getFormatter().format(b))
        .orElse(StringUtils.EMPTY);
  }

  private static DecimalFormat getFormatter() {
    DecimalFormat df = new DecimalFormat("#.########");
    df.setParseBigDecimal(true);
    return df;
  }
}
