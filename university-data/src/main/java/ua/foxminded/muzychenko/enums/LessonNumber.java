package ua.foxminded.muzychenko.enums;

import lombok.Getter;

@Getter
public enum LessonNumber {
    FIRST("1st lesson", "8:30"),
    SECOND("2nd lesson", "10:25"),
    THIRD("3rd lesson", "12:20"),
    FOURTH("4th lesson", "14:15"),
    FIFTH("5th lesson", "16:10"),
    SIXTH("6th lesson", "18:30");

    private final String displayValue;
    private final String timeValue;

    LessonNumber(String displayValue, String timeValue) {
        this.displayValue = displayValue;
        this.timeValue = timeValue;
    }
}
