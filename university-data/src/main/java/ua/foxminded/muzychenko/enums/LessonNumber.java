package ua.foxminded.muzychenko.enums;

import lombok.Getter;

@Getter
public enum LessonNumber {
    FIRST("1st lesson"),
    SECOND("2nd lesson"),
    THIRD("3rd lesson"),
    FOURTH("4th lesson"),
    FIFTH("5th lesson"),
    SIXTH("6th lesson");

    private final String displayValue;

    LessonNumber(String displayValue) {
        this.displayValue = displayValue;
    }
}
