package com.base.seed.dal.mapper;

import com.base.seed.dal.entity.UserDo;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserDoMapper {

  int deleteByPrimaryKey(Long id);

  int insert(UserDo record);

  int insertSelective(UserDo record);

  UserDo selectByPrimaryKey(Long id);

  int updateByPrimaryKeySelective(UserDo record);

  int updateByPrimaryKey(UserDo record);

  List<UserDo> findAllByHireDate(@Param("hireDate") Date hireDate);

  List<UserDo> findAllByFirstName(@Param("firstName") String firstName);
}