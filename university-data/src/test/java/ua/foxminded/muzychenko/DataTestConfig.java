package ua.foxminded.muzychenko;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import javax.sql.DataSource;

@TestConfiguration
@EnableTransactionManagement
@Import(DataConfiguration.class)
public class DataTestConfig {

    @Container
    private final static PostgreSQLContainer<?> postgreSQLContainer =
        new PostgreSQLContainer<>("postgres:latest");

    static {
        postgreSQLContainer.withReuse(true);
        postgreSQLContainer.start();
    }

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();

        config.setDriverClassName(org.postgresql.Driver.class.getName());
        config.setJdbcUrl(postgreSQLContainer.getJdbcUrl());
        config.setUsername(postgreSQLContainer.getUsername());
        config.setPassword(postgreSQLContainer.getPassword());
        config.setMaximumPoolSize(2);

        return new HikariDataSource(config);
    }

    @Bean(initMethod = "migrate")
    public Flyway flyway() {
        return Flyway.configure()
            .dataSource(dataSource())
            .locations("classpath:db/migration/test")
            .load();
    }
}
