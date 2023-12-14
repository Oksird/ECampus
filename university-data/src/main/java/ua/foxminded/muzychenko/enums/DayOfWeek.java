package ua.foxminded.muzychenko.enums;

import lombok.Getter;

@Getter
public enum DayOfWeek {
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday");

    private final String displayValue;

    DayOfWeek(String displayValue) {
        this.displayValue = displayValue;
    }

}
