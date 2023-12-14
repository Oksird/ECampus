package ua.foxminded.muzychenko.service.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = LessonInfoMapper.class)
class LessonInfoMapperTest {

    @MockBean
    private TeacherProfileMapper teacherMapper;
    @Autowired
    private LessonInfoMapper mapper;


}