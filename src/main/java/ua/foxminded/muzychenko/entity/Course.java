package ua.foxminded.muzychenko.entity;

import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

@Data
public final class Course {
    @NonNull
    private UUID courseId;
    private String courseName;
    private String courseDescription;

    public Course(@NonNull UUID courseId , String courseName, String courseDescription) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
    }
}
