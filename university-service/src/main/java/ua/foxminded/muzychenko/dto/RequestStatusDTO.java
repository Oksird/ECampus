package ua.foxminded.muzychenko.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ua.foxminded.muzychenko.enums.RequestStatusEnum;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class RequestStatusDTO {
    private String id;
    private RequestStatusEnum status;
}
