package ua.foxminded.muzychenko;

import com.ibatis.common.jdbc.ScriptRunner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import ua.foxminded.muzychenko.exception.DataBaseRunTimeException;

public class DBCreator {

    private static final String DROP_DATABASE_QUERY = "DROP DATABASE IF EXISTS school";
    private static final String DROP_USER_QUERY = "DROP USER IF EXISTS fox";
    private static final String CLOSE_CONNECTION =
        "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = 'school';";
    private final DBConnector dbConnector;

    public DBCreator(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    public void createTables() {
        try (Connection connection = dbConnector.getConnection()) {
            ScriptRunner scriptRunner = new ScriptRunner(connection, false, false);
            Reader createTablesScriptFile = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(
                    DBConnector.class.getClassLoader().getResourceAsStream("create-tables.sql")),
                StandardCharsets.UTF_8));
            scriptRunner.runScript(createTablesScriptFile);

        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (SQLException sqlException) {
            throw new DataBaseRunTimeException(sqlException);
        }
    }

    public static void createDB() {
        DBConnector dbConnector = new DBConnector();
        try (Connection postgresConnection = dbConnector.getConnection();
            Statement postgresStatement = postgresConnection.createStatement()) {
            ScriptRunner scriptRunner = new ScriptRunner(postgresConnection, false, false);
            postgresStatement.execute(CLOSE_CONNECTION);
            postgresStatement.executeUpdate(DROP_DATABASE_QUERY);
            postgresStatement.execute(DROP_USER_QUERY);
            Reader createDataBasesScriptFile = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(
                    DBConnector.class.getClassLoader()
                        .getResourceAsStream("create-data-bases.sql")),
                StandardCharsets.UTF_8));

            scriptRunner.runScript(createDataBasesScriptFile);


        } catch (SQLException e) {
            throw new DataBaseRunTimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
