package hello.controller;

import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import hello.service.UserService;



import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {
    
    private static final Logger log = LoggerFactory.getLogger(HelloController.class);

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Autowired
    UserService userService;

    @Autowired
    private DataSource dataSource;

    @RequestMapping("/")
    public String index() {
        return "Spring Boot Demo Application";
    }

    @RequestMapping("/userDetail")
    String db(Map<String, Object> model) {
      log.info("Reached userDetail");
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      // stmt.executeUpdate("CREATE TABLE IF NOT EXISTS USER_DETAIL ID INT PRIMARY KEY NOT NULL, NAME VARCHAR(45))");
      // stmt.executeUpdate("INSERT INTO USER_DETAIL VALUES (1, 'Jack')");
      // stmt.executeUpdate("INSERT INTO USER_DETAIL VALUES (2, 'Brayn')");
      ResultSet rs = stmt.executeQuery("SELECT NAME FROM USER_DETAIL");

      ArrayList<String> output = new ArrayList<String>();
      while (rs.next()) {
        output.add("Read from DB: " + rs.getString("NAME"));
      }

    //   model.put("records", output);
      return output.stream().collect(Collectors.joining("; "));
    } catch (Exception e) {
      e.printStackTrace();
    //   model.put("message", );
      return "error: " + e.getMessage();
    }
  }
  
  @Bean
  public DataSource dataSource() throws SQLException {
    if (dbUrl == null || dbUrl.isEmpty()) {
      return new HikariDataSource();
    } else {
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(dbUrl);
      return new HikariDataSource(config);
    }
  }
}
