package ua.foxminded.muzychenko.dao.impl;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.foxminded.muzychenko.DBConnector;
import ua.foxminded.muzychenko.dao.GroupDao;
import ua.foxminded.muzychenko.entity.GroupEntity;
import ua.foxminded.muzychenko.exception.DataBaseRunTimeException;
import ua.foxminded.muzychenko.exception.WrongFilePathException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GroupDaoImplTest {
    private GroupDao groupDao;
    private static final String RESOURCES_PATH = "src/main/resources/";

    @BeforeEach
    void setUp() {

        DBConnector dbConnector = new DBConnector("/testDb.properties");
        ScriptRunner scriptRunner;

        groupDao = new GroupDaoImpl(dbConnector);

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

    @DisplayName("Groups with less or equal count of students are found")
    @Test
    void findGroupWithLessOrEqualStudents_shouldReturnAllGroupsWithLessOrEqualStudents() {
        List<GroupEntity> groups = groupDao.findGroupWithLessOrEqualStudents(4);
        List<GroupEntity> expectedGroups = new ArrayList<>(
            List.of(
                new GroupEntity(1, "AB-01"),
                new GroupEntity(2, "AB-02"),
                new GroupEntity(3, "AB-03")

            )
        );

        assertEquals(expectedGroups, groups);
    }
    @DisplayName("No groups with less or equal count of students")
    @Test
    void findGroupWithLessOrEqualStudents_shouldReturnEmptyListIfConditionWasNotCompleted() {
        List<GroupEntity> emptyList = new ArrayList<>();
        assertEquals(emptyList, groupDao.findGroupWithLessOrEqualStudents(1));
    }

    @DisplayName("Group was created")
    @Test
    void insert_shouldCreateNewGroup() {
        GroupEntity expectedGroup = new GroupEntity(4,"TEST-7777");
        groupDao.create(expectedGroup);
        GroupEntity actualGroup = null;
        Optional<GroupEntity> groupEntityOptional = groupDao.findById(4L);
        if (groupEntityOptional.isPresent()) {
            actualGroup = groupEntityOptional.get();
        }
        assertEquals(expectedGroup, actualGroup);
    }

    @DisplayName("Group is updated")
    @Test
    void update_shouldReplaceGroupWithProvided() {
        GroupEntity group = new GroupEntity(1, "TEST-7777");
        Optional<GroupEntity> realGroupOptional = groupDao.findById(1L);
        GroupEntity realGroup = null;
        if (realGroupOptional.isPresent()) {
            realGroup = realGroupOptional.get();
        }
        groupDao.update(realGroup, group);
        assertEquals(groupDao.findById(1L).get(), group);
    }

    @DisplayName("Group was deleted")
    @Test
    void deleteById() {
        GroupEntity testGroup = new GroupEntity(4, "TEST-7777");
        List<GroupEntity> groups = groupDao.findAll();
        groupDao.create(testGroup);
        groups.add(testGroup);
        assertEquals(groups, groupDao.findAll());
        groupDao.deleteById(4L);
        groups.remove(testGroup);
        assertEquals(groups, groupDao.findAll());
    }
}
