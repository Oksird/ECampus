package ua.foxminded.muzychenko.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_requests")
@EqualsAndHashCode
@Entity
public class UserRequest {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private AbstractUser user;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private RequestType type;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private RequestStatus status;
}
