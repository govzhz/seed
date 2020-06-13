package com.base.seed.webapp;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SeedApplication.class)
public class BaseTest {

  @Before
  public void beforeTest() {
    System.out.println("\n\n==================== 开始单元测试 ====================\n");
  }

  @After
  public void afterTest() {
    System.out.println("\n==================== 单元测试结束 ====================\n\n");
  }
}
