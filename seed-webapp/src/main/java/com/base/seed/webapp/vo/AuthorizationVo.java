package com.base.seed.webapp.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author zz 2019/12/12
 */
@Data
@AllArgsConstructor
public class AuthorizationVo {

    private String token;

    private String userName;
}
