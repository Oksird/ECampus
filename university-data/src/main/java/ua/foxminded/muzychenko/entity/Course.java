package ua.foxminded.muzychenko.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"groups", "teacher"})
@ToString(exclude = {"groups", "teacher"})
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", referencedColumnName = "user_id")
    private Teacher teacher;

    @ManyToMany(mappedBy = "courses")
    private List<Group> groups = new ArrayList<>();

    public Course(@NonNull UUID courseId , String courseName, String courseDescription) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
    }
}
