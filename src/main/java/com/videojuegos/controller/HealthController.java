package com.videojuegos.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {

  private final DataSource dataSource;

  @Value("${spring.datasource.url}")
  private String url;

  @Value("${spring.datasource.username}")
  private String username;

  public HealthController(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @GetMapping("/db")
  public Map<String, Object> dbHealth() {
    Map<String, Object> res = new LinkedHashMap<>();
    res.put("database", "mysql");
    res.put("url", url);
    res.put("username", username);

    try (Connection c = dataSource.getConnection()) {
      boolean ok = c.isValid(2);
      res.put("status", ok ? "UP" : "DOWN");
      return res;
    } catch (SQLException e) {
      res.put("status", "DOWN");
      res.put("errorType", e.getClass().getName());
      res.put("sqlState", e.getSQLState());
      res.put("vendorCode", e.getErrorCode());
      res.put("message", e.getMessage());
      if (e.getCause() != null) {
        res.put("causeType", e.getCause().getClass().getName());
        res.put("causeMessage", e.getCause().getMessage());
      }
      return res;
    } catch (Exception e) {
      res.put("status", "DOWN");
      res.put("errorType", e.getClass().getName());
      res.put("message", e.getMessage());
      if (e.getCause() != null) {
        res.put("causeType", e.getCause().getClass().getName());
        res.put("causeMessage", e.getCause().getMessage());
      }
      return res;
    }
  }
}
