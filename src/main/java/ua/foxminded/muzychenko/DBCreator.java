package ua.foxminded.muzychenko;

import org.apache.ibatis.jdbc.ScriptRunner;
import ua.foxminded.muzychenko.exception.WrongFilePathException;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class DBCreator {

    private final ScriptRunner scriptRunner;

    public DBCreator(ScriptRunner scriptRunner) {
        this.scriptRunner = scriptRunner;
        this.scriptRunner.setAutoCommit(true);
    }

    public void createTables() {
        runScript("createTables.sql");
    }

    public void createDB() {
        runScript("createDataBases.sql");
    }

    public void runScript(String scriptFileName) {

        String resourcesPath = "src/main/resources/";

        try {
            FileReader createTablesSQLScriptFile = new FileReader(resourcesPath + scriptFileName);
            scriptRunner.runScript(createTablesSQLScriptFile);
        } catch (FileNotFoundException fileNotFoundException) {
            throw new WrongFilePathException(fileNotFoundException.getMessage());
        }
    }
}
