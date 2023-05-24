package ua.foxminded.muzychenko.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.foxminded.muzychenko.DBConnector;
import ua.foxminded.muzychenko.dao.GroupDao;
import ua.foxminded.muzychenko.dao.GroupsGenerator;
import ua.foxminded.muzychenko.entity.GroupEntity;
import ua.foxminded.muzychenko.exception.DataBaseRunTimeException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


class GroupsGeneratorImplTest {

    private GroupsGenerator groupsGenerator;
    private GroupDao groupDao;

    @BeforeEach
    void setUp(){
        DBConnector dbConnector = new DBConnector("/testdb.properties");
        groupDao = new GroupDaoImpl(dbConnector);
        groupsGenerator = new GroupsGeneratorImpl(groupDao, new Random());

        try (Connection conn = dbConnector.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(
                "CREATE TABLE IF NOT EXISTS groups (" +
                    "    group_id SERIAL PRIMARY KEY," +
                    "    group_name VARCHAR(50) NOT NULL UNIQUE" +
                    ")"
            );

            stmt.execute("DELETE FROM groups");

        } catch (SQLException e) {
            throw new DataBaseRunTimeException(e);
        }
    }


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

    @Test
    void insertGroups_shouldInsertMultipleGroups() {
        List<GroupEntity> groups = new ArrayList<>();
        groups.add(new GroupEntity(0L, "CS-01"));
        groups.add(new GroupEntity(0L, "CS-02"));
        groups.add(new GroupEntity(0L, "CS-03"));

        groupsGenerator.insertGroups(groups);

        List<GroupEntity> insertedGroups = groupDao.findAll();
        assertEquals(groups.size(), insertedGroups.size());

        for (int i = 0; i < groups.size(); i++) {
            GroupEntity expectedGroup = groups.get(i);
            GroupEntity actualGroup = insertedGroups.get(i);

            assertEquals(expectedGroup.groupName(), actualGroup.groupName());
        }
    }

    @Test
    void insertGroups_shouldInsertSingleGroup() {
        GroupEntity group = new GroupEntity(0L, "CS-01");

        groupsGenerator.insertGroups(Collections.singletonList(group));

        List<GroupEntity> insertedGroups = groupDao.findAll();
        assertEquals(1, insertedGroups.size());

        GroupEntity insertedGroup = insertedGroups.get(0);
        assertEquals(group.groupName(), insertedGroup.groupName());
    }


}
