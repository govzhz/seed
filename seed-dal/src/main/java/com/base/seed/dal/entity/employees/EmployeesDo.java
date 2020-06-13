package com.base.seed.dal.entity.employees;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeesDo {

  private Integer empNo;

  private Date birthDate;

  private String firstName;

  private String lastName;

  private String gender;

  private Date hireDate;

  private Integer uniId;
}