package com.base.seed.webapp.controller;

import com.base.seed.common.PageParam;
import com.base.seed.dal.entity.UserDo;
import com.base.seed.service.UserService;
import com.base.seed.webapp.support.RestEntity;
import com.base.seed.webapp.vo.UserChangePwdVo;
import com.github.pagehelper.Page;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping(value = "/users")
  public RestEntity<List<UserDo>> listUsers(@Validated PageParam param) {
    List<UserDo> users = userService.listUsers(param);
    return RestEntity.ok(users, ((Page<UserDo>) users).getTotal());
  }

  @PostMapping(value = "/users/{userId}/change-pwd")
  public RestEntity<Void> changePwd(@PathVariable("userId") String userId,
      @Validated @RequestBody UserChangePwdVo userChangePwdVo) {
    userService.changePwd(userId, userChangePwdVo.getOldPwd(), userChangePwdVo.getNewPwd());
    return RestEntity.ok();
  }
}
