package ua.foxminded.muzychenko.enums;

import lombok.Getter;

@Getter
public enum RequestTypeEnum {
    BECOME_STUDENT("Become a student"),
    BECOME_TEACHER("Become a teacher"),
    BECOME_STAFF("Become a staff");

    private final String displayValue;

    RequestTypeEnum(String displayValue) {
        this.displayValue = displayValue;
    }

}
