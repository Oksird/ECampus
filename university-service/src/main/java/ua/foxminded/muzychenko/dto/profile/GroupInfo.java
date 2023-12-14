package ua.foxminded.muzychenko.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class GroupInfo {
    private String groupId;
    private String groupName;
    private Integer countOfStudents;
    private List<CourseInfo> courses;
}
