package ua.foxminded.muzychenko.service.validator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.foxminded.muzychenko.dto.profile.GroupInfo;
import ua.foxminded.muzychenko.exception.GroupNotFoundException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = GroupValidator.class)
class GroupValidatorTest {

    @Autowired
    private GroupValidator validator;

    @Test
    void validateGroupNameShouldThrowExceptionIfGroupNameIsInvalid() {
        String invalidGroupName = "9k>922";
        GroupInfo groupInfo = new GroupInfo(
            UUID.randomUUID().toString(),
            invalidGroupName
        );

        assertThrows(GroupNotFoundException.class, () -> validator.validateGroupName(groupInfo));
    }

    @Test
    void validateGroupNameShouldNotThrowExceptionIfGroupNameIsValid() {
        String validGroupName = "AA-11";
        GroupInfo groupInfo = new GroupInfo(
            UUID.randomUUID().toString(),
            validGroupName
        );

        assertDoesNotThrow(() -> validator.validateGroupName(groupInfo));
    }
}