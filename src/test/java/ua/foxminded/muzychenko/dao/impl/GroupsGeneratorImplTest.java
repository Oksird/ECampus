package ua.foxminded.muzychenko.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.foxminded.muzychenko.DBConnector;
import ua.foxminded.muzychenko.dao.GroupDao;
import ua.foxminded.muzychenko.dao.GroupsGenerator;
import ua.foxminded.muzychenko.entity.GroupEntity;
import util.DataBaseSetUpper;

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

    @BeforeEach
    void setUp() {

        DBConnector dbConnector = new DBConnector("/testDb.properties");

        groupDao = new GroupDaoImpl(dbConnector);
        groupsGenerator = new GroupsGeneratorImpl(groupDao, new Random());

        DataBaseSetUpper.setUpDataBase(dbConnector);
    }

    @DisplayName("Groups were generated successfully")
    @Test
    void generateDataShouldReturnListOfTenGroupsWithSpecificNames() {
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
    void insertGroupsShouldInsertMultipleGroups() {
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