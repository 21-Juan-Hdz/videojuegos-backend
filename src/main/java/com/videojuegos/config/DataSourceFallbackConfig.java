package com.videojuegos.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.time.Duration;

@Configuration
public class DataSourceFallbackConfig {

  @Value("${spring.datasource.url}")
  private String url;

  @Value("${spring.datasource.username}")
  private String username;

  @Value("${spring.datasource.password}")
  private String password;

  @Value("${spring.datasource.driver-class-name:com.mysql.cj.jdbc.Driver}")
  private String driver;

  @Value("${spring.datasource.hikari.initialization-fail-timeout:-1}")
  private long initializationFailTimeout;

  @Bean
  @ConditionalOnMissingBean(DataSource.class)
  public DataSource dataSource() {
    HikariConfig cfg = new HikariConfig();
    cfg.setDriverClassName(driver);
    cfg.setJdbcUrl(url);
    cfg.setUsername(username);
    cfg.setPassword(password);

    cfg.setConnectionTimeout(Duration.ofSeconds(2).toMillis());
    cfg.setValidationTimeout(Duration.ofSeconds(2).toMillis());
    cfg.setInitializationFailTimeout(initializationFailTimeout);

    cfg.setMinimumIdle(0);
    cfg.setMaximumPoolSize(5);
    cfg.setPoolName("videojuegos-mysql-pool");
    return new HikariDataSource(cfg);
  }
}
