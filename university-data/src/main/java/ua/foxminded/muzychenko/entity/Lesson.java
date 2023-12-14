package ua.foxminded.muzychenko.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lessons")
@EqualsAndHashCode
@Entity
@ToString
public class Lesson {

    @Id
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_type_id", referencedColumnName = "id")
    private LessonType lessonType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", referencedColumnName = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_day_id", referencedColumnName = "id")
    private StudyDay studyDay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_week_id", referencedColumnName = "id")
    private StudyWeek studyWeek;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_time_id", referencedColumnName = "id")
    private LessonTime lessonTime;

    @Column(name = "additional_info")
    private String additionalInfo;
}
