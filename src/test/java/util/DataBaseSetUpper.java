package util;

import org.apache.ibatis.jdbc.ScriptRunner;
import ua.foxminded.muzychenko.DBConnector;
import ua.foxminded.muzychenko.exception.DataBaseRunTimeException;
import ua.foxminded.muzychenko.exception.WrongFilePathException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;

public class DataBaseSetUpper {

    private static final String RESOURCES_PATH = "src/main/resources/";

    public static void setUpDataBase(DBConnector dbConnector) {
        ScriptRunner scriptRunner;
        try {
            scriptRunner = new ScriptRunner(dbConnector.getConnection());
        } catch (SQLException sqlException) {
            throw new DataBaseRunTimeException(sqlException);
        }

        try {
            FileReader createTablesSQLScriptFile = new FileReader(RESOURCES_PATH + "createTables.sql");
            FileReader generateDataSQLScriptFile = new FileReader(RESOURCES_PATH + "generateTestData.sql");
            FileReader deleteAllDataFromDataBaseSQLFile
                = new FileReader(RESOURCES_PATH + "deleteAllDataFromDataBases.sql");
            scriptRunner.runScript(createTablesSQLScriptFile);
            scriptRunner.runScript(deleteAllDataFromDataBaseSQLFile);
            scriptRunner.runScript(generateDataSQLScriptFile);
        } catch (FileNotFoundException fileNotFoundException) {
            throw new WrongFilePathException(fileNotFoundException.getMessage());
        }
    }
}
