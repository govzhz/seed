package com.base.seed.webapp.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

  @Bean
  public Docket buildDocket() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(buildApiInf())
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.base.seed"))
        .build();
  }

  private ApiInfo buildApiInf() {
    return new ApiInfoBuilder()
        .title("基础骨架")
        .termsOfServiceUrl("")
        .description("项目接口描述").build();
  }
}
