package ua.foxminded.muzychenko.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ua.foxminded.muzychenko.enums.LessonNumber;

import java.time.LocalTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@Table(name = "lesson_times")
@EqualsAndHashCode
@Entity
@ToString
public class LessonTime {

    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "lesson_number", nullable = false)
    private LessonNumber lessonNumber;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;
}
