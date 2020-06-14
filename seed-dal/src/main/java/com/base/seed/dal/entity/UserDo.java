package com.base.seed.dal.entity;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDo {

  private Long id;

  private String userId;

  private String firstName;

  private String lastName;

  private Boolean gender;

  private Date hireDate;

  private Date gmtCreate;

  private Date gmtModified;
}