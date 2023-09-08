package ua.foxminded.muzychenko.university.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@Table(name = "groups")
@EqualsAndHashCode(exclude = {"students", "lessons"})
@Entity
@ToString(exclude = {"students", "lessons"})
public final class Group {
    @Id
    @Column(name = "group_id", nullable = false, updatable = false)
    private UUID groupId;

    @Column(name = "group_name", nullable = false)
    private String groupName;

    @OneToMany(mappedBy = "group", cascade = CascadeType.REMOVE)
    private List<Student> students = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.REMOVE)
    private Set<Lesson> lessons = new HashSet<>();

    public Group(@NonNull UUID groupId , String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }
}
