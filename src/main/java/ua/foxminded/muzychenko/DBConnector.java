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

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new DataBaseRunTimeException(e);
        }
    }
}
