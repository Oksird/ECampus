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
import ua.foxminded.muzychenko.enums.TypeOfLesson;

import java.util.UUID;

@Data
@NoArgsConstructor
@Table(name = "lesson_types")
@EqualsAndHashCode
@Entity
@ToString
public class LessonType {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "lesson_type", nullable = false)
    private TypeOfLesson type;
}
