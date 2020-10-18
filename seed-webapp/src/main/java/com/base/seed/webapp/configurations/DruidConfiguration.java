package com.base.seed.webapp.configurations;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.base.seed.common.properties.DruidProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 监控：http://127.0.0.1:8081/druid/index.html
 * 整合：https://github.com/alibaba/druid/tree/master/druid-spring-boot-starter
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(DruidProperties.class)
public class DruidConfiguration {

  @Autowired
  private DruidProperties druidProperties;

  /**
   * Druid 连接池配置
   */
  @Bean
  public DruidDataSource dataSource() {
    DruidDataSource datasource = new DruidDataSource();
    datasource.setUrl(druidProperties.getUrl());
    datasource.setUsername(druidProperties.getUsername());
    datasource.setPassword(druidProperties.getPassword());
    datasource.setDriverClassName(druidProperties.getDriverClassName());
    datasource.setInitialSize(druidProperties.getInitialSize());
    datasource.setMinIdle(druidProperties.getMinIdle());
    datasource.setMaxActive(druidProperties.getMaxActive());
    datasource.setMaxWait(druidProperties.getMaxWait());
    datasource.setTimeBetweenEvictionRunsMillis(druidProperties.getTimeBetweenEvictionRunsMillis());
    datasource.setMinEvictableIdleTimeMillis(druidProperties.getMinEvictableIdleTimeMillis());
    datasource.setMaxEvictableIdleTimeMillis(druidProperties.getMaxEvictableIdleTimeMillis());
    datasource.setValidationQuery(druidProperties.getValidationQuery());
    datasource.setTestWhileIdle(druidProperties.isTestWhileIdle());
    datasource.setTestOnBorrow(druidProperties.isTestOnBorrow());
    datasource.setTestOnReturn(druidProperties.isTestOnReturn());
    datasource.setPoolPreparedStatements(druidProperties.isPoolPreparedStatements());
    datasource.setMaxPoolPreparedStatementPerConnectionSize(druidProperties.getMaxPoolPreparedStatementPerConnectionSize());
    try {
      datasource.setFilters(druidProperties.getFilters());
    } catch (Exception e) {
      log.error("druid configuration initialization filter", e);
    }
    datasource.setConnectionProperties(druidProperties.getConnectionProperties());
    return datasource;
  }

  /**
   * JDBC操作配置
   */
  @Bean(name = "dataOneTemplate")
  public JdbcTemplate jdbcTemplate(@Autowired DruidDataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }

  /**
   * 配置 Druid 监控界面
   */
  @Bean
  public ServletRegistrationBean statViewServlet() {
    ServletRegistrationBean srb =
        new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
    //设置控制台管理用户
    srb.addInitParameter("loginUsername", "root");
    srb.addInitParameter("loginPassword", "root");
    //是否可以重置数据
    srb.addInitParameter("resetEnable", "false");
    return srb;
  }

  @Bean
  public FilterRegistrationBean statFilter() {
    //创建过滤器
    FilterRegistrationBean frb =
        new FilterRegistrationBean(new WebStatFilter());
    //设置过滤器过滤路径
    frb.addUrlPatterns("/*");
    //忽略过滤的形式
    frb.addInitParameter("exclusions",
        "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
    return frb;
  }
}