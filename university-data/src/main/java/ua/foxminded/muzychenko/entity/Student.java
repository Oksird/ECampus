package ua.foxminded.muzychenko.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true, exclude = {"group"})
@ToString(callSuper = true, exclude = {"group"})
@Data
@Entity
@NoArgsConstructor
@DiscriminatorValue("ROLE_STUDENT")
public class Student extends AbstractUser {
    private static final String USER_ROLE = "ROLE_STUDENT";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    public Student(@NonNull UUID studentId , String firstName, String lastName, String email, String password,
                   Group group, String phoneNumber, String address) {
        super(studentId, USER_ROLE, firstName, lastName, email, password, phoneNumber, address);
        this.group = group;
    }
}
