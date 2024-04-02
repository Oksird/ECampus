package ua.foxminded.muzychenko.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class UserRequestDTO {
    private String id;
    private String userId;
    private String userRole;
    private RequestTypeDTO requestTypeDTO;
    private RequestStatusDTO requestStatusDTO;
}
