package ua.foxminded.muzychenko.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true, exclude = {"group", "courses"})
@ToString(callSuper = true, exclude = {"group", "courses"})
@Data
@Entity
@NoArgsConstructor
@DiscriminatorValue("Student")
public class Student extends User {
    private static final String USER_TYPE = "Student";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "students_courses",
        joinColumns = {
            @JoinColumn(name = "student_id", referencedColumnName = "user_id")
        },
        inverseJoinColumns = {
            @JoinColumn(name = "course_id", referencedColumnName = "course_id")
        }
    )
    private Set<Course> courses = new HashSet<>();

    public Student(@NonNull UUID studentId , String firstName, String lastName, String email, String password,
                   Group group) {
        super(studentId, USER_TYPE, firstName, lastName, email, password);
        this.group = group;
    }
}
