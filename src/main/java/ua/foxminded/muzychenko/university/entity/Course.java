package ua.foxminded.muzychenko.university.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"students", "teachers", "lessons"})
@ToString(exclude = {"students", "teachers", "lessons"})
@Table(name = "courses")
@Entity
public class Course {
    @Id
    @Column(name = "course_id", updatable = false, nullable = false)
    private UUID courseId;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "course_description")
    private String courseDescription;

    @ManyToMany(mappedBy = "courses", cascade = CascadeType.REMOVE)
    private Set<Student> students = new HashSet<>();

    @ManyToMany(mappedBy = "courses", cascade = CascadeType.REMOVE)
    private Set<Teacher> teachers = new HashSet<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    private Set<Lesson> lessons = new HashSet<>();

    public Course(@NonNull UUID courseId , String courseName, String courseDescription) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
    }
}
