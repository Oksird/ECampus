package ua.foxminded.muzychenko.enums;

import lombok.Getter;

@Getter
public enum TypeOfLesson {
    LECTURE("Lecture"),
    LABORATORY_WORK("Lab work"),
    PRACTICE("Practice"),
    SEMINAR("Seminar");

    private final String displayValue;

    TypeOfLesson(String displayValue) {
        this.displayValue = displayValue;
    }
}
