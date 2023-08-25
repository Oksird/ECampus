package ua.foxminded.muzychenko.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true, exclude = "courses")
@Data
@NoArgsConstructor
@Entity
@DiscriminatorValue("Teacher")
public class Teacher extends User {
    private static final String USER_TYPE = "Teacher";

    @ManyToMany
    @JoinTable(
        name = "teachers_courses",
        joinColumns = {
            @JoinColumn(name = "teacher_id", referencedColumnName = "user_id")
        },
        inverseJoinColumns = {
            @JoinColumn(name = "course_id", referencedColumnName = "course_id")
        }
    )
    private Set<Course> courses = new HashSet<>();

    public Teacher(@NonNull UUID teacherId, String firstName, String lastName, String email, String password) {
        super(teacherId, USER_TYPE, firstName, lastName, email, password);
    }
}
