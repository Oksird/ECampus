package ua.foxminded.muzychenko;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.muzychenko.dao.CourseDao;
import ua.foxminded.muzychenko.dao.GroupDao;
import ua.foxminded.muzychenko.dao.StudentDao;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SchoolApplicationFacade {

    private final GroupDao groupDao;
    private final StudentDao studentDao;
    private final CourseDao courseDao;

    public void runQueries() {
        studentDao.findAll();
        courseDao.findAll();
        groupDao.findAll();
    }
}
