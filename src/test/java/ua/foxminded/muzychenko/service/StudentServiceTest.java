package ua.foxminded.muzychenko.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ua.foxminded.muzychenko.TestConfig;
import ua.foxminded.muzychenko.dao.StudentDao;

@SpringJUnitConfig(TestConfig.class)
class StudentServiceTest {
    @MockBean
    private StudentDao studentDao;

    @Autowired
    private StudentService studentService;

}