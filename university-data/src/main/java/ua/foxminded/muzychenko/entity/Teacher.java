package ua.foxminded.muzychenko.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@DiscriminatorValue("ROLE_TEACHER")

public class Teacher extends AbstractUser {
    private static final String USER_ROLE = "ROLE_TEACHER";

    @OneToOne(mappedBy = "teacher")
    private Course course;

    public Teacher(@NonNull UUID teacherId, String firstName, String lastName, String email,
                   String password, String phoneNumber, String address) {
        super(teacherId, USER_ROLE, firstName, lastName, email, password, phoneNumber, address);
    }
}
