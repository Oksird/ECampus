package ua.foxminded.muzychenko;

import org.apache.ibatis.jdbc.ScriptRunner;
import ua.foxminded.muzychenko.exception.WrongFilePathException;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class TablesCreator {

    private final ScriptRunner scriptRunner;

    public TablesCreator(ScriptRunner scriptRunner) {
        this.scriptRunner = scriptRunner;
        this.scriptRunner.setAutoCommit(true);
    }

    public void createTables() {
        String resourcesPath = "src/main/resources/";

        try {
            FileReader createTablesSQLScriptFile = new FileReader(resourcesPath + "createTables.sql");
            scriptRunner.runScript(createTablesSQLScriptFile);
        } catch (FileNotFoundException fileNotFoundException) {
            throw new WrongFilePathException(fileNotFoundException.getMessage());
        }
    }
}
