package ua.foxminded.muzychenko.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
@Table(name = "groups")
@EqualsAndHashCode(exclude = {"students"})
@Entity
@ToString(exclude = {"students"})
public final class Group {

    @Id
    @Column(name = "group_id", nullable = false, updatable = false)
    private UUID groupId;

    @Column(name = "group_name", nullable = false)
    private String groupName;

    @OneToMany(mappedBy = "group", cascade = CascadeType.REMOVE)
    private List<Student> students = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "groups_courses",
        joinColumns = {
            @JoinColumn(name = "group_id", referencedColumnName = "group_id")
        },
        inverseJoinColumns = {
            @JoinColumn(name = "course_id", referencedColumnName = "course_id")
        }
    )
    private List<Course> courses = new ArrayList<>();

    public Group(@NonNull UUID groupId , String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }
}
