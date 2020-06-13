package com.base.seed.dal.mapper.employees;

import com.base.seed.dal.entity.employees.EmployeesDo;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeesDoMapper {

  int deleteByPrimaryKey(Integer empNo);

  int insert(EmployeesDo record);

  int insertSelective(EmployeesDo record);

  EmployeesDo selectByPrimaryKey(Integer empNo);

  int updateByPrimaryKeySelective(EmployeesDo record);

  int updateByPrimaryKey(EmployeesDo record);

  List<EmployeesDo> selectByBirthDate();
}