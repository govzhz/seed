package com.base.seed.common;

import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author zhangzheng 2020/6/14
 */
@Data
public class PageParam {

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
}
