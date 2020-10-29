package com.base.seed.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.base.seed.common.PageParam;
import com.base.seed.common.annos.RetryAttemptChecker;
import com.base.seed.common.enums.RetryAttemptType;
import com.base.seed.common.exception.IllegalPasswordException;
import com.base.seed.dal.entity.UserDo;
import com.base.seed.dal.mapper.UserDoMapper;
import com.base.seed.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserDoMapper userDoMapper;

  @SentinelResource(value = "com.base.seed.service.impl.UserServiceImpl.listUsers", blockHandler = "handlerListUsersBlock")
  @Override
  public PageInfo<UserDo> listUsers(PageParam<Void> pageParam) {
    return PageHelper.startPage(pageParam.getPage(), pageParam.getSize(), true)
        .doSelectPageInfo(() -> userDoMapper.findAllByFirstName("Li"));
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

  public PageInfo<UserDo> handlerListUsersBlock(PageParam<Void> pageParam, BlockException e) {
    if (e instanceof FlowException) {
      // 由流控规则触发的异常，快速失败模式下直接触发，排队模式下超时后触发
      log.warn("List users has been flow control.");
    } else if (e instanceof DegradeException) {
      // 由熔断降级规则触发的异常
      log.warn("List users has been degraded.");
    }
    return new PageInfo<>();
  }
}
