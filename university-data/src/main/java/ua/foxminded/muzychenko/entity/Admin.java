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
@DiscriminatorValue("ROLE_ADMIN")
public class Admin extends AbstractUser {
    private static final String USER_ROLE = "ROLE_ADMIN";
    public Admin(@NonNull UUID adminId , String firstName, String lastName, String email, String password,
                 String phoneNumber, String address) {
        super(adminId, USER_ROLE, firstName, lastName, email, password, phoneNumber, address);
    }
}
