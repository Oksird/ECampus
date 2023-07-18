package ua.foxminded.muzychenko;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.muzychenko.dao.CourseDao;
import ua.foxminded.muzychenko.dao.GroupDao;
import ua.foxminded.muzychenko.dao.StudentDao;

@Component
public class SchoolApplicationFacade {

    private final GroupDao groupDao;
    private final StudentDao studentDao;
    private final CourseDao courseDao;

    @Autowired
    public SchoolApplicationFacade(GroupDao groupDao, CourseDao courseDao,
                                   StudentDao studentDao) {
        this.groupDao = groupDao;
        this.courseDao = courseDao;
        this.studentDao = studentDao;

    }

    public void runQueries() {
        studentDao.findAll();
        courseDao.findAll();
        groupDao.findAll();
    }
}
