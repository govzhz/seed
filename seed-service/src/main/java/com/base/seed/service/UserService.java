package com.base.seed.service;

import com.base.seed.common.PageParam;
import com.base.seed.dal.entity.UserDo;
import com.github.pagehelper.PageInfo;

public interface UserService {

  PageInfo<UserDo> listUsers(PageParam<Void> pageParam);

  void changePwd(String userId, String oldPwd, String newPwd);
}
