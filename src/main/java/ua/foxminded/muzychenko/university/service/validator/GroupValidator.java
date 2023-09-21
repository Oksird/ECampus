package ua.foxminded.muzychenko.university.service.validator;

import ua.foxminded.muzychenko.university.config.Validator;
import ua.foxminded.muzychenko.university.dao.exception.GroupNotFoundException;
import ua.foxminded.muzychenko.university.dto.profile.GroupInfo;

import java.util.regex.Pattern;

@Validator
public class GroupValidator {
    private static final Pattern GROUP_NAME_PATTERN = Pattern.compile("^[A-Z]{2}-\\d{2}$", Pattern.MULTILINE);

    public void validateGroupName(GroupInfo groupInfo) {
        if (!GROUP_NAME_PATTERN.matcher(groupInfo.getGroupName()).matches()) {
            throw new GroupNotFoundException();
        }
    }
}
