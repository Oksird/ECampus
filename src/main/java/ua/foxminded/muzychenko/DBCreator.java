package ua.foxminded.muzychenko;

import org.apache.ibatis.jdbc.ScriptRunner;
import ua.foxminded.muzychenko.exception.WrongFilePathException;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class DBCreator {

    private static final String RESOURCES_PATH = "src/main/resources/";
    private final ScriptRunner scriptRunner;

    public DBCreator(ScriptRunner scriptRunner) {
        this.scriptRunner = scriptRunner;
        this.scriptRunner.setAutoCommit(true);
    }

    public void createTables() {
        try {
            FileReader generateDataSQLScriptFile = new FileReader(RESOURCES_PATH + "createTables.sql");
            scriptRunner.runScript(generateDataSQLScriptFile);
        } catch (FileNotFoundException fileNotFoundException) {
            throw new WrongFilePathException(fileNotFoundException.getMessage());
        }
    }

    public void createDB() {
        try {
            FileReader createTablesSQLScriptFile = new FileReader(RESOURCES_PATH + "createDataBases.sql");
            scriptRunner.runScript(createTablesSQLScriptFile);
        } catch (FileNotFoundException fileNotFoundException) {
            throw new WrongFilePathException(fileNotFoundException.getMessage());
        }
    }
}
