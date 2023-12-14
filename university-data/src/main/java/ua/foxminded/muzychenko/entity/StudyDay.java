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
import ua.foxminded.muzychenko.enums.DayOfWeek;

import java.util.UUID;

@Data
@NoArgsConstructor
@Table(name = "study_days")
@EqualsAndHashCode
@Entity
@ToString
public class StudyDay {

    @Id
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false, updatable = false, unique = true)
    private DayOfWeek dayOfWeek;
}
