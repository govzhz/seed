package com.base.seed.service.demo;

import com.base.seed.dal.entity.employees.EmployeesDo;
import com.base.seed.dal.mapper.employees.EmployeesDoMapper;
import com.github.pagehelper.PageHelper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeesService {

  @Autowired
  private EmployeesDoMapper employeesDoMapper;

  public List<EmployeesDo> employees() {
    PageHelper.startPage(1, 10, true);
    return employeesDoMapper.selectByBirthDate();
  }
}
