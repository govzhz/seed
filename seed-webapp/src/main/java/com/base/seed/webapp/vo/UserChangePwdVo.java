package com.base.seed.webapp.vo;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserChangePwdVo {

  @NotBlank
  private String oldPwd;

  @NotBlank
  private String newPwd;
}
