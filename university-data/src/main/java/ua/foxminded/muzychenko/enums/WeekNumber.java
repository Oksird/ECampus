package ua.foxminded.muzychenko.enums;

import lombok.Getter;

@Getter
public enum WeekNumber {
    FIRST("1st week"),
    SECOND("2nd week");

    private final String displayValue;

    WeekNumber(String displayValue) {
        this.displayValue = displayValue;
    }
}
