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
import ua.foxminded.muzychenko.enums.WeekNumber;

import java.util.UUID;

@Data
@NoArgsConstructor
@Table(name = "study_weeks")
@EqualsAndHashCode
@Entity
@ToString
public class StudyWeek {

    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "week_number", nullable = false)
    private WeekNumber weekNumber;
}
