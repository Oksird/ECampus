package ua.foxminded.muzychenko.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class GroupEntity {
    private long groupId;
    private String groupName;
}
