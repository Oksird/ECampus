package ua.foxminded.muzychenko.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ua.foxminded.muzychenko.DBConnector;
import ua.foxminded.muzychenko.dao.StudentDao;
import ua.foxminded.muzychenko.entity.StudentEntity;
import ua.foxminded.muzychenko.exception.DataBaseRunTimeException;
import util.DataBaseSetUpper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class StudentDaoImplTest {

    private StudentDao studentDao;
    private StudentDao studentDaoException;
    private final DBConnector dbConnector = new DBConnector("/testDb.properties");

    @BeforeEach
    void setUp() {

        studentDao = new StudentDaoImpl(dbConnector);
        DBConnector dbConnectorException = Mockito.mock(DBConnector.class);
        studentDaoException = new StudentDaoImpl(dbConnectorException);

        try {
            when(dbConnectorException.getConnection()).thenThrow(new SQLException());
        } catch (SQLException sqlException) {
            throw new DataBaseRunTimeException(sqlException);
        }

        DataBaseSetUpper.setUpDataBase(dbConnector);
    }

    @DisplayName("All students are found by course")
    @Test
    void findByCourseShouldReturnAllStudentOnSpecifiedCourse() {
        List<StudentEntity> expectedStudents = new ArrayList<>(
            List.of(
                new StudentEntity(1, 1, "John", "Brown"),
                new StudentEntity(3,1,"Mija", "White"),
                new StudentEntity(4,1,"Lee", "Simpson"),
                new StudentEntity(6,2,"Jorge", "White"),
                new StudentEntity(9,3,"David", "Ivanko"))
        );
        List<StudentEntity> actualStudents = studentDao.findByCourse("Math");
        assertEquals(expectedStudents, actualStudents);
    }

    @DisplayName("Course doesn't exist")
    @Test
    void findByCourseShouldReturnEmptyListIfCourseDoesntExist() {
        List<StudentEntity> students = studentDao.findByCourse("Computer Science");
        assertEquals(0, students.size());
    }

    @DisplayName("Student is added to course")
    @Test
    void addToCourseShouldAddSpecificStudentToSpecificCourse() {
        StudentEntity student = new StudentEntity(
            13,
            1,
            "Maksym",
            "Muzychenko");
        studentDao.create(student);
        studentDao.addToCourse(student, "Math");

        List<StudentEntity> expectedStudents = new ArrayList<>(
            List.of(
                new StudentEntity(1, 1, "John", "Brown"),
                new StudentEntity(3,1,"Mija", "White"),
                new StudentEntity(4,1,"Lee", "Simpson"),
                new StudentEntity(6,2,"Jorge", "White"),
                new StudentEntity(9,3,"David", "Ivanko"),
                student)
        );

        assertEquals(expectedStudents, studentDao.findByCourse("Math"));
    }

    @DisplayName("Student is removed from course")
    @Test
    void removeFromCourseShouldRemoveSpecificStudentFromSpecificCourse() {
        Optional<StudentEntity> student = studentDao.findById(1L);
        student.ifPresent(studentEntity -> studentDao.removeFromCourse(studentEntity, "Math"));
        List<StudentEntity> expectedStudents = new ArrayList<>(
            List.of(
                new StudentEntity(3,1,"Mija", "White"),
                new StudentEntity(4,1,"Lee", "Simpson"),
                new StudentEntity(6,2,"Jorge", "White"),
                new StudentEntity(9,3,"David", "Ivanko"))
        );

        assertEquals(expectedStudents, studentDao.findByCourse("Math"));
    }

    @DisplayName("Student is deleted by id")
    @Test
    void deleteByIdShouldDeleteSpecificStudent() {
        List<StudentEntity> expectedStudents = studentDao.findAll();
        expectedStudents.remove(new StudentEntity(1, 1, "John", "Brown"));

        studentDao.deleteById(1L);

        assertEquals(expectedStudents, studentDao.findAll());
    }

    @DisplayName("Student is updated correctly")
    @Test
    void updateShouldReplaceOldStudentEntityWithNewOne() {
        List<StudentEntity> students = studentDao.findAll();
        StudentEntity firstStudent = students.get(0);
        StudentEntity expectedStudent = new StudentEntity(1, 1, "Sam", "Winchester");
        studentDao.update(firstStudent, expectedStudent);
        StudentEntity actualStudent = null;
        Optional<StudentEntity> optionalStudent = studentDao.findById(1L);
        if (optionalStudent.isPresent()){
            actualStudent = optionalStudent.get();
        }
        assertEquals(expectedStudent, actualStudent);
    }

    @DisplayName("Pagination")
    @Test
    void findAllByPageShouldReturnAllStudentsOnTheCurrentPage() {
        long countOfPages = 4;
        long pageSize = 4;
        List<StudentEntity> studentsOnLastPage = studentDao.findAllByPage(countOfPages, pageSize);
        List<StudentEntity> actualStudents = new ArrayList<>();
        Optional<StudentEntity> student;
        for (int i = (int) (pageSize * 2); i >studentDao.findAll().size() ; i++) {
            student = studentDao.findById((long) i);
            student.ifPresent(actualStudents::add);
        }
        assertEquals(studentsOnLastPage, actualStudents);
    }

    @DisplayName("create() trow sql exception")
    @Test
    void createShouldThrowSQLException() {
        assertThrows(DataBaseRunTimeException.class,
            () -> studentDaoException.create(new StudentEntity(55, 1, "Test", "Test"))
        );
    }

    @DisplayName("update() trow sql exception")

    @Test
    void updateShouldThrowSQLException() {
        assertThrows(DataBaseRunTimeException.class,
            () -> studentDaoException
                .update(studentDao.findById(1L)
                        .get(),
                    new StudentEntity(
                        1L,
                        1,
                        "Test",
                        "Test"))
        );
    }

    @DisplayName("delete() trow sql exception")
    @Test
    void deleteShouldThrowSQLException() {
        assertThrows(DataBaseRunTimeException.class,
            () -> studentDaoException
                .deleteById(1L)
        );
    }

    @DisplayName("findAll() trow sql exception")
    @Test
    void findAllShouldThrowSQLException() {
        assertThrows(DataBaseRunTimeException.class,
            () -> studentDaoException
                .findAll()
        );
    }

    @DisplayName("findAllByPage() trow sql exception")
    @Test
    void findAllByPageShouldThrowSQLException() {
        assertThrows(DataBaseRunTimeException.class,
            () -> studentDaoException
                .findAllByPage(444444L, 54444L)
        );
    }

    @DisplayName("createAll() trow sql exception")
    @Test
    void createAllShouldThrowSQLException() {
        assertThrows(DataBaseRunTimeException.class,
            () -> studentDaoException
                .createAll(new ArrayList<>(List.of(
                        new StudentEntity(
                            111L,
                            1,
                            "Test1",
                            "Test1"),
                        new StudentEntity(
                            112L,
                            1,
                            "Test2",
                            "Test2")
                    )
                    )
                )
        );
    }

    @DisplayName("findByCourse() trow sql exception")
    @Test
    void findByCourseShouldThrowSQLException() {
        assertThrows(DataBaseRunTimeException.class,
            () -> studentDaoException
                .findByCourse("Math")
        );
    }

    @DisplayName("removeFromCourse() trow sql exception")
    @Test
    void removeFromCourseShouldThrowSQLException() {
        assertThrows(DataBaseRunTimeException.class,
            () -> studentDaoException
                .removeFromCourse(studentDao.findById(1L).get(), "Math")
        );
    }

    @DisplayName("addToCourse() trow sql exception")
    @Test
    void addToCourseShouldThrowSQLException() {
        assertThrows(DataBaseRunTimeException.class,
            () -> studentDaoException
                .addToCourse(studentDao.findById(1L).get(), "Mathssss")
        );
    }

    @DisplayName("String consumer throws exception")
    @Test
    void findByCourseShouldThrowSQLExceptionInPreparedStatement() throws SQLException {
        DBConnector dbConnectorForSpecificException = Mockito.mock(DBConnector.class);
        Connection connection = Mockito.mock(Connection.class);
        when(dbConnectorForSpecificException.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        doThrow(new SQLException()).when(preparedStatement).setString(anyInt(), anyString());
        studentDao = new StudentDaoImpl(dbConnectorForSpecificException);
        assertThrows(DataBaseRunTimeException.class,
            () -> studentDao
                .findByCourse("Math")
        );
    }

    @DisplayName("Long consumer throws exception")
    @Test
    void findAllByPageShouldMapResultSetToEntityAndAddToList() throws SQLException {
        DBConnector dbConnectorForSpecificException = Mockito.mock(DBConnector.class);
        Connection connection = Mockito.mock(Connection.class);
        when(dbConnectorForSpecificException.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        when(preparedStatement.getResultSet()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        List<StudentEntity> result = studentDao.findAllByPage(1L, 10L);
        assertEquals(10, result.size());
    }

    @DisplayName("Find by id thrown SQL exception cause of result set is empty")
    @Test
    void findByIdShouldThrowSQLExceptionWhenOptionalIsEmpty() throws SQLException {
        DBConnector dbConnectorForSpecificException = Mockito.mock(DBConnector.class);
        Connection connection = Mockito.mock(Connection.class);

        when(dbConnectorForSpecificException.getConnection()).thenReturn(connection);

        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        ResultSet resultSet = Mockito.mock(ResultSet.class);

        when(preparedStatement.getResultSet()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Optional<StudentEntity> result = studentDao.findById(14L);

        assertFalse(result.isPresent(), "Expected empty Optional");
    }
}
