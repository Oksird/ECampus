package ua.foxminded.muzychenko.university.dao.exception;

public class GroupNotFoundException extends RuntimeException {
    public GroupNotFoundException() {
        super("Group not found with this group name");
    }
}
