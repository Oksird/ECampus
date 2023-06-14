package ua.foxminded.muzychenko.dao.impl;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.foxminded.muzychenko.DBConnector;
import ua.foxminded.muzychenko.dao.GroupDao;
import ua.foxminded.muzychenko.dao.GroupsGenerator;
import ua.foxminded.muzychenko.entity.GroupEntity;
import ua.foxminded.muzychenko.exception.DataBaseRunTimeException;
import ua.foxminded.muzychenko.exception.WrongFilePathException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GroupsGeneratorImplTest {

    private GroupDao groupDao;
    private GroupsGenerator groupsGenerator;
    private static final String RESOURCES_PATH = "src/main/resources/";

    @BeforeEach
    void setUp() {

        DBConnector dbConnector = new DBConnector("/testDb.properties");
        ScriptRunner scriptRunner;

        groupDao = new GroupDaoImpl(dbConnector);
        groupsGenerator = new GroupsGeneratorImpl(groupDao, new Random());

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

    @DisplayName("Groups were generated successfully")
    @Test
    void generateData_shouldReturnListOfTenGroupsWithSpecificNames() {
        List<GroupEntity> groups = groupsGenerator.generateData();
        assertNotNull(groups);
        String pattern = "[A-Z]{2}-\\d{2}";
        for (GroupEntity group : groups) {
            assertTrue(Pattern.matches(pattern, group.groupName()));
        }
        assertEquals(10, groups.size());
    }

    @DisplayName("Groups were inserted")
    @Test
    void insertGroups_shouldInsertMultipleGroups() {
        List<GroupEntity> groups = new ArrayList<>();
        groups.add(new GroupEntity(4L, "TEST-01"));
        groups.add(new GroupEntity(5L, "TEST-02"));
        groups.add(new GroupEntity(6L, "TEST-03"));

        groupsGenerator.insertGroups(groups);
        List<GroupEntity> insertedGroups = groupDao.findAll();

        for (int i = 3; i < groups.size(); i++) {

            GroupEntity expectedGroup = groups.get(i);
            GroupEntity actualGroup = insertedGroups.get(i);

            assertEquals(expectedGroup.groupName(), actualGroup.groupName());
        }
    }

}