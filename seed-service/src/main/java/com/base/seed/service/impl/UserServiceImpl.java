package com.base.seed.service.impl;

import com.base.seed.common.PageParam;
import com.base.seed.common.annos.RetryAttemptChecker;
import com.base.seed.common.enums.RetryAttemptType;
import com.base.seed.common.exception.IllegalPasswordException;
import com.base.seed.dal.entity.UserDo;
import com.base.seed.dal.mapper.UserDoMapper;
import com.base.seed.service.UserService;
import com.github.pagehelper.PageHelper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserDoMapper userDoMapper;

  @Override
  public List<UserDo> listUsers(PageParam pageParam) {
    PageHelper.startPage(pageParam.getPage(), pageParam.getSize(), true);
    return userDoMapper.findAllByFirstName("Li");
  }

  @Override
  @RetryAttemptChecker(key = "#userId",
      retryAttemptType = RetryAttemptType.MODIFY_PASSWORD, retryForException = IllegalPasswordException.class)
  public void changePwd(String userId, String oldPwd, String newPwd) {
    if ("U123".equals(userId) && "123".equals(oldPwd)) {
      log.info("User[{}] change password success.", userId);
    } else {
      throw new IllegalPasswordException();
    }
  }
}
