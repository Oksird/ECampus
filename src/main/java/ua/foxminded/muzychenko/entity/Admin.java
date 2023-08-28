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
@DiscriminatorValue("Admin")
public class Admin extends User {
    private static final String USER_TYPE = "Admin";
    public Admin(@NonNull UUID adminId , String firstName, String lastName, String email, String password) {
        super(adminId,USER_TYPE, firstName, lastName, email, password);
    }

}
