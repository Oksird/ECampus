package ua.foxminded.muzychenko.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.foxminded.muzychenko.enums.RequestStatusEnum;

import java.util.UUID;

@Entity
@Data
@Table(name = "request_statuses")
@AllArgsConstructor
@NoArgsConstructor
public class RequestStatus {

    @Id
    @Column(name = "id")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RequestStatusEnum status;
}
