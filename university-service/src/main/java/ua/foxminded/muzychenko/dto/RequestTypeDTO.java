package ua.foxminded.muzychenko.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ua.foxminded.muzychenko.enums.RequestTypeEnum;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class RequestTypeDTO {
    private String id;
    private RequestTypeEnum type;
}
