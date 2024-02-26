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
import ua.foxminded.muzychenko.enums.RequestTypeEnum;

import java.util.UUID;

@Data
@Entity
@Table(name = "request_types")
@AllArgsConstructor
@NoArgsConstructor
public class RequestType {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private RequestTypeEnum type;

}
