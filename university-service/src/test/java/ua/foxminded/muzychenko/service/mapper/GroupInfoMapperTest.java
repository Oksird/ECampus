package ua.foxminded.muzychenko.service.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.foxminded.muzychenko.dto.profile.GroupInfo;
import ua.foxminded.muzychenko.entity.Group;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = GroupInfoMapper.class)
class GroupInfoMapperTest {

    @Autowired
    private GroupInfoMapper mapper;

    @Test
    void mapGroupEntityToGroupInfoShouldReturnGroupInfoBasedOnGroupEntity() {
        Group group = new Group(
            UUID.randomUUID(),
            "gn"
        );

        GroupInfo expectedGroupInfo = new GroupInfo(
            group.getGroupId().toString(),
            group.getGroupName(),
            group.getStudents().size()
        );

        assertEquals(expectedGroupInfo, mapper.mapGroupEntityToGroupInfo(group));
    }
}