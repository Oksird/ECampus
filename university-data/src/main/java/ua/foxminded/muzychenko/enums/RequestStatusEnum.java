package ua.foxminded.muzychenko.enums;

import lombok.Getter;

@Getter
public enum RequestStatusEnum {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected");

    private final String displayValue;

    RequestStatusEnum(String displayValue) {
        this.displayValue = displayValue;
    }
}
