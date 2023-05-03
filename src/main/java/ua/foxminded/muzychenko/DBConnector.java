package ua.foxminded.muzychenko;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import ua.foxminded.muzychenko.exception.DataBaseRunTimeException;

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

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new DataBaseRunTimeException(e);
        }
    }

    public void closeConnection() {
        dataSource.close();
    }
    
}