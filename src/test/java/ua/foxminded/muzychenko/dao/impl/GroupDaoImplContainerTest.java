package ua.foxminded.muzychenko.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.muzychenko.DBConnector;
import ua.foxminded.muzychenko.dao.GroupDao;
import ua.foxminded.muzychenko.entity.GroupEntity;
import ua.foxminded.muzychenko.exception.DataBaseRunTimeException;
import util.DataBaseSetUpper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@Testcontainers
class GroupDaoImplContainerTest {

    @Container
    private final PostgreSQLContainer container = new PostgreSQLContainer("postgres:latest");

    private GroupDao groupDao;

    @BeforeEach
    void set_up() {
        DBConnector dbConnector = new DBConnector(
            container.getJdbcUrl(),
            container.getUsername(),
            container.getPassword()
        );
        groupDao = new GroupDaoImpl(dbConnector);
        DataBaseSetUpper.setUpDataBase(dbConnector);
    }

    @DisplayName("Groups with less or equal count of students are found")
    @Test
    void findGroupWithLessOrEqualStudentsShouldReturnAllGroupsWithLessOrEqualStudents() {
        List<GroupEntity> groups = groupDao.findGroupWithLessOrEqualStudents(4);
        List<GroupEntity> expectedGroups = new ArrayList<>(
            List.of(
                new GroupEntity(1, "AB-01"),
                new GroupEntity(2, "AB-02"),
                new GroupEntity(3, "AB-03")

            )
        );

        assertTrue(groups.containsAll(expectedGroups));
    }
    @DisplayName("No groups with less or equal count of students")
    @Test
    void findGroupWithLessOrEqualStudentsShouldReturnEmptyListIfConditionWasNotCompleted() {
        List<GroupEntity> emptyList = new ArrayList<>();
        assertEquals(emptyList, groupDao.findGroupWithLessOrEqualStudents(1));
    }

    @DisplayName("Group was created")
    @Test
    void insertShouldCreateNewGroup() {
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
    void updateShouldReplaceGroupWithProvided() {
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
    void deleteByIdShouldDeleteGroupById() {
        GroupEntity testGroup = new GroupEntity(4, "TEST-7777");
        List<GroupEntity> groups = groupDao.findAll();
        groupDao.create(testGroup);
        groups.add(testGroup);
        assertEquals(groups, groupDao.findAll());
        groupDao.deleteById(4L);
        groups.remove(testGroup);
        assertEquals(groups, groupDao.findAll());
    }

    @DisplayName("Find group throws exception")
    @Test
    void findGroupWithLessOrEqualsStudentsShouldThrowSQLException() throws SQLException {
        DBConnector dbConnectorForSpecificException = Mockito.mock(DBConnector.class);
        Connection connection = Mockito.mock(Connection.class);
        when(dbConnectorForSpecificException.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        doThrow(new SQLException()).when(preparedStatement).setLong(anyInt(), anyLong());
        groupDao = new GroupDaoImpl(dbConnectorForSpecificException);
        assertThrows(DataBaseRunTimeException.class,
            () -> groupDao
                .findGroupWithLessOrEqualStudents(11)
        );
    }
}
