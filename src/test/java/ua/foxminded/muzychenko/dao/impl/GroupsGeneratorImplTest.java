package ua.foxminded.muzychenko.dao.impl;

import org.junit.jupiter.api.Test;
import ua.foxminded.muzychenko.DBConnector;
import ua.foxminded.muzychenko.dao.GroupsGenerator;
import ua.foxminded.muzychenko.entity.GroupEntity;

import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class GroupsGeneratorImplTest {

    private final GroupsGenerator groupGenerator = new GroupsGeneratorImpl(
        new GroupDaoImpl(new DBConnector()),
        new Random()
    );

    @Test
    void generateData_shouldReturnListOfTenGroupsWithSpecificNames() {
        List<GroupEntity> groups = groupGenerator.generateData();
        assertNotNull(groups);
        String pattern = "[A-Z]{2}-\\d{2}";
        for (GroupEntity group : groups) {
            assertTrue(Pattern.matches(pattern, group.groupName()));
        }
        assertEquals(10, groups.size());
    }

    @Test
    void insertGroups() {
    }

}
