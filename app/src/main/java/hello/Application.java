package hello;

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

@SpringBootApplication
public class Application {
    
    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Autowired
    private DataSource dataSource;

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
        log.info("Application Started Successully !!!");
        
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
        output.add("Read from DB: " + rs.getTimestamp("NAME"));
      }

      model.put("records", output);
      return "db";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  // private static Connection getConnection() throws URISyntaxException, SQLException {
  //   String dbUrl = System.getenv("JDBC_DATABASE_URL");
  //   return DriverManager.getConnection(dbUrl);
  // }
  
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

  // @Bean
  // public DataSource dataSource() throws SQLException {
  //   String databaseUrl = System.getenv("JDBC_DATABASE_URL");
  //     log.info("Initializing PostgreSQL database: {}", databaseUrl);

  //     URI dbUri;
  //     try {
  //         dbUri = new URI(databaseUrl);
  //     }
  //     catch (URISyntaxException e) {
  //         log.error(String.format("Invalid JDBC_DATABASE_URL: %s", databaseUrl), e);
  //         return null;
  //     }

  //     String username = dbUri.getUserInfo().split(":")[0];
  //     String password = dbUri.getUserInfo().split(":")[1];
  //     String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' 
  //                     + dbUri.getPort() + dbUri.getPath();

  //     DataSource dataSource = new DataSource();
  //     dataSource.setDriverClassName("org.postgresql.Driver");
  //     dataSource.setUrl(dbUrl);
  //     dataSource.setUsername(username);
  //     dataSource.setPassword(password);
  //     dataSource.setTestOnBorrow(true);
  //     dataSource.setTestWhileIdle(true);
  //     dataSource.setTestOnReturn(true);
  //     dataSource.setValidationQuery("SELECT 1");
  //     return dataSource;
  //   }

  // @Bean
  //   @Profile("postgres")
  // public DataSource postgresDataSource() {
  //     String databaseUrl = System.getenv("DATABASE_URL")
  //     log.info("Initializing PostgreSQL database: {}", databaseUrl);

  //     URI dbUri;
  //     try {
  //         dbUri = new URI(databaseUrl);
  //     }
  //     catch (URISyntaxException e) {
  //         log.error(String.format("Invalid DATABASE_URL: %s", databaseUrl), e);
  //         return null;
  //     }

  //     String username = dbUri.getUserInfo().split(":")[0];
  //     String password = dbUri.getUserInfo().split(":")[1];
  //     String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' 
  //                     + dbUri.getPort() + dbUri.getPath();

  //     DataSource dataSource = new DataSource();
  //     dataSource.setDriverClassName("org.postgresql.Driver");
  //     dataSource.setUrl(dbUrl);
  //     dataSource.setUsername(username);
  //     dataSource.setPassword(password);
  //     dataSource.setTestOnBorrow(true);
  //     dataSource.setTestWhileIdle(true);
  //     dataSource.setTestOnReturn(true);
  //     dataSource.setValidationQuery("SELECT 1");
  //     return dataSource;
  //   }

}
