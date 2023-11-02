package ua.foxminded.muzychenko.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@DiscriminatorValue("ROLE_STAFF")
public class Staff extends AbstractUser {

    private static final String USER_ROLE = "ROLE_STAFF";

    public Staff(@NonNull UUID staffId , String firstName, String lastName, String email, String password) {
        super(staffId, USER_ROLE, firstName, lastName, email, password);
    }

}
