package ua.foxminded.muzychenko.entity;

import lombok.Data;
import lombok.NonNull;

import java.util.UUID;


@Data
public final class Group {
    @NonNull
    private UUID groupId;
    private String groupName;

    public Group(@NonNull UUID groupId , String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }
}
