package com.base.seed.service.demo.strategy;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zz 2019/12/16
 */
@AllArgsConstructor
@Getter
public enum IdentityEnum {

    VIP(1, "VIP用户"),
    NORMAL(2, "普通用户")
    ;

    private Integer code;
    private String msg;
}
