package com.base.seed.common;

import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PageParam<T> {

  /**
   * 页码
   */
  @NotNull
  private Integer page;

  /**
   * 页大小
   */
  @NotNull
  private Integer size;

  /**
   * 分页数据
   */
  private T date;
}
