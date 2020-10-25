package com.base.seed.service.domain;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * 秒杀活动详情
 */
@Data
public class PromotionDetail {

  /**
   * 活动唯一编号
   */
  private Long id;

  /**
   * 秒杀活动名
   */
  private String name;

  /**
   * 参与秒杀的商品
   */
  private List<PromotionProductItem> products;

  /**
   * 秒杀开始时间
   */
  private LocalDateTime startTime;

  /**
   * 秒杀结束时间
   */
  private LocalDateTime endTime;

  /**
   * 秒杀上架状态
   */
  private boolean isEnabled;

  /**
   * 是否秒杀结束
   */
  private boolean finished;
}
