package ua.foxminded.muzychenko.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true, exclude = {"courses", "lessons"})
@ToString(callSuper = true, exclude = {"courses", "lessons"})
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

    @OneToMany(mappedBy = "teacher")
    private Set<Lesson> lessons;

    public Teacher(@NonNull UUID teacherId, String firstName, String lastName, String email, String password) {
        super(teacherId, USER_TYPE, firstName, lastName, email, password);
    }
}
