package com.base.seed.webapp.controller;

import com.base.seed.common.PageParam;
import com.base.seed.dal.entity.UserDo;
import com.base.seed.service.demo.UserService;
import com.base.seed.webapp.support.RestEntity;
import com.github.pagehelper.Page;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping(value = "/users")
  public RestEntity<List<UserDo>> listUsers(@Validated PageParam param) {
    List<UserDo> users = userService.listUsers(param);
    return RestEntity.success(users, ((Page<UserDo>) users).getTotal());
  }
}
