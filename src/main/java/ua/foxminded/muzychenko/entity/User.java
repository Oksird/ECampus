package ua.foxminded.muzychenko.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
public abstract class User {
    @Id
    @Column(name = "user_id", updatable = false, nullable = false)
    protected UUID userId;

    @Column(name = "user_type", insertable = false, updatable = false)
    protected String userType;

    @Column(name = "first_name")
    protected String firstName;

    @Column(name = "last_name")
    protected String lastName;

    @Column(name = "email")
    protected String email;

    @Column(name = "password")
    protected String password;

    protected User(@NonNull UUID userId, @NonNull String userType, String firstName, String lastName, String email, String password) {
        this.userId = userId;
        this.userType = userType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
