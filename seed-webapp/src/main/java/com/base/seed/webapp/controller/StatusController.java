package com.base.seed.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StatusController {

  @ResponseBody
  @RequestMapping(value = "/status.jsp")
  public String status() {
    return "ok";
  }
}
