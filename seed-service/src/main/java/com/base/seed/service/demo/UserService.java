package com.base.seed.service.demo;

import com.base.seed.common.PageParam;
import com.base.seed.dal.entity.UserDo;
import com.base.seed.dal.mapper.UserDoMapper;
import com.github.pagehelper.PageHelper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  private UserDoMapper userDoMapper;

  public List<UserDo> listUsers(PageParam pageParam) {
    PageHelper.startPage(pageParam.getPage(), pageParam.getSize(), true);
    return userDoMapper.findAllByFirstName("Li");
  }
}
