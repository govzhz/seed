package com.base.seed.webapp.service;

import com.base.seed.service.demo.observer.RegisterPublisher;
import com.base.seed.webapp.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RegisterTest extends BaseTest {

  @Autowired
  private RegisterPublisher registerPublisher;

  @Test
  public void test() {
    registerPublisher.register("Mr.Zh");
  }
}
