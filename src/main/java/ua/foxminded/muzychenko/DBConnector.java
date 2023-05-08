package ua.foxminded.muzychenko;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConnector {

    private final HikariDataSource dataSource;

    public DBConnector(String fileConfigName) {
        HikariConfig hikariConfig = new HikariConfig(fileConfigName);
        dataSource = new HikariDataSource(hikariConfig);
    }

    public DBConnector() {
        HikariConfig postgresConfig = new HikariConfig();
        postgresConfig.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
        postgresConfig.setUsername("postgres");
        postgresConfig.setPassword("password");
        dataSource = new HikariDataSource(postgresConfig);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    public void closeConnection() {
        dataSource.close();
    }
}
