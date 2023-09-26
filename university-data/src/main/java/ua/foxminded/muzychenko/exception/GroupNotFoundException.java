package ua.foxminded.muzychenko.exception;

public class GroupNotFoundException extends RuntimeException {
    public GroupNotFoundException() {
        super("Group not found with this group name");
    }
}
