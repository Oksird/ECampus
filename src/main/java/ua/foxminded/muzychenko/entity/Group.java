package ua.foxminded.muzychenko.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@Table(name = "groups")
@Entity
public final class Group {
    @Id
    @Column(name = "group_id", nullable = false, updatable = false)
    private UUID groupId;

    @Column(name = "group_name")
    private String groupName;

    @OneToMany(mappedBy = "group")
    private List<Student> students = new ArrayList<>();

    public Group(@NonNull UUID groupId , String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }
}
