package ua.foxminded.muzychenko;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Container;
import ua.foxminded.muzychenko.config.AppConfiguration;

import javax.sql.DataSource;

@TestConfiguration
@Import(AppConfiguration.class)
@ComponentScan
@EnableTransactionManagement
public class TestConfig {

    @Container
    private final static PostgreSQLContainer<?> postgreSQLContainer =
        new PostgreSQLContainer<>("postgres:latest");

    static {
        postgreSQLContainer.start();

        var containerDelegate = new JdbcDatabaseDelegate(postgreSQLContainer, "");

        ScriptUtils.runInitScript(containerDelegate, "sql/createTestTables.sql");
        ScriptUtils.runInitScript(containerDelegate, "sql/generateTestData.sql");
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariDataSource();
        hikariConfig.setJdbcUrl(postgreSQLContainer.getJdbcUrl());
        hikariConfig.setUsername(postgreSQLContainer.getUsername());
        hikariConfig.setPassword(postgreSQLContainer.getPassword());
        return new HikariDataSource(hikariConfig);
    }
}
