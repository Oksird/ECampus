package ua.foxminded.muzychenko.repository;

import org.springframework.stereotype.Repository;
import ua.foxminded.muzychenko.entity.Student;

import java.util.List;

@Repository
public interface StudentRepository extends UserRepository<Student> {

    List<Student> findByGroup_GroupName(String groupName);

}
