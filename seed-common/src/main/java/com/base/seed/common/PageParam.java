package com.base.seed.common;

import javax.validation.constraints.NotNull;

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

  public void setPage(Integer page) {
    this.page = page;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public Integer getPage() {
    return page;
  }

  public Integer getSize() {
    return size;
  }
}
