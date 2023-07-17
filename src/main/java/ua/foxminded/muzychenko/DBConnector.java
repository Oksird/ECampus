package ua.foxminded.muzychenko;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Component
@PropertySource("classpath:db.properties")
public class DBConnector {
    private final HikariDataSource dataSource;

    public DBConnector(String fileConfigName) {
        HikariConfig hikariConfig = new HikariConfig(fileConfigName);
        dataSource = new HikariDataSource(hikariConfig);
    }

    @Autowired
    public DBConnector(
        @Value("${jdbcUrl}")
        String jdbcUrl,
        @Value("${dataSource.user}")
        String userName,
        @Value("${dataSource.password}")
        String password) {

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setUsername(userName);
        hikariConfig.setPassword(password);
        dataSource = new HikariDataSource(hikariConfig);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
