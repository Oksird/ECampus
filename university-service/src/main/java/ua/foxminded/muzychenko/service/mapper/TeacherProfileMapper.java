package ua.foxminded.muzychenko.service.mapper;

import lombok.AllArgsConstructor;
import ua.foxminded.muzychenko.config.Mapper;
import ua.foxminded.muzychenko.dto.profile.TeacherProfile;
import ua.foxminded.muzychenko.entity.Teacher;

@Mapper
@AllArgsConstructor
public class TeacherProfileMapper {

    public TeacherProfile mapTeacherEntityToProfile(Teacher teacher) {

        return new TeacherProfile(
            teacher.getUserId().toString(),
            teacher.getFirstName(),
            teacher.getLastName(),
            teacher.getEmail(),
            teacher.getPhoneNumber(),
            teacher.getAddress()
        );
    }
}
