package com.base.seed.dal.mapper.employees;

import com.base.seed.dal.entity.employees.EmployeesDo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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