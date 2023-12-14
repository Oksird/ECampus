package ua.foxminded.muzychenko.service.mapper;

import lombok.AllArgsConstructor;
import ua.foxminded.muzychenko.config.Mapper;
import ua.foxminded.muzychenko.dto.profile.GroupInfo;
import ua.foxminded.muzychenko.dto.profile.StudentProfile;
import ua.foxminded.muzychenko.entity.Group;
import ua.foxminded.muzychenko.entity.Student;

@Mapper
@AllArgsConstructor
public class StudentProfileMapper {

    private GroupInfoMapper groupInfoMapper;

    public StudentProfile mapStudentInfoToProfile(Student student) {
        Group group = student.getGroup();

        GroupInfo groupInfo = null;

        if (group != null) {
            groupInfo = groupInfoMapper.mapGroupEntityToGroupInfo(group);
        }

        return new StudentProfile(
            student.getUserId().toString(),
            student.getFirstName(),
            student.getLastName(),
            student.getEmail(),
            groupInfo,
            student.getPhoneNumber(),
            student.getAddress()
        );
    }
}
