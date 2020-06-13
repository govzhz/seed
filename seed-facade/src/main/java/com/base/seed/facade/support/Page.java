package com.base.seed.facade.support;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Page<T> implements Serializable {

  private static final long serialVersionUID = 560583713978903017L;

  /**
   * 查询总数
   **/
  private Long totalCount;

  /**
   * 查询页数据
   **/
  private List<T> data;
}
