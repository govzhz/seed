package com.base.seed.service;

import com.base.seed.common.PageParam;
import com.base.seed.dal.entity.UserDo;
import java.util.List;

public interface UserService {

  List<UserDo> listUsers(PageParam pageParam);

  void changePwd(String userId, String oldPwd, String newPwd);
}
