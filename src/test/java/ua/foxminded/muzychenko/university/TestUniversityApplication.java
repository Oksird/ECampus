package ua.foxminded.muzychenko.university;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import ua.foxminded.muzychenko.university.config.AppConfiguration;

import javax.sql.DataSource;

@TestConfiguration
@Import(AppConfiguration.class)
@ComponentScan
@EnableTransactionManagement
public class TestUniversityApplication {

    @Container
    private final static PostgreSQLContainer<?> postgreSQLContainer =
        new PostgreSQLContainer<>("postgres:latest");

    static {
        postgreSQLContainer.start();
    }

    @Bean
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariDataSource();
        hikariConfig.setJdbcUrl(postgreSQLContainer.getJdbcUrl());
        hikariConfig.setUsername(postgreSQLContainer.getUsername());
        hikariConfig.setPassword(postgreSQLContainer.getPassword());
        return new HikariDataSource(hikariConfig);
    }

    @Bean(initMethod = "migrate")
    public Flyway flyway() {
        return Flyway.configure()
            .dataSource(dataSource())
            .locations("classpath:db/migration/test")
            .load();
    }
}
