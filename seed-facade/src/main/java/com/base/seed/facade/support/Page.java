/**
 * Copyright（C）, 2011-2019, 微贷网
 */
package com.base.seed.facade.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分页响应类
 *
 * @param <T>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Page<T> implements Serializable {

    private static final long serialVersionUID = 560583713978903017L;

    /** 查询总数 **/
    private Long totalCount;

    /** 查询页数据 **/
    private List<T> data;
}
