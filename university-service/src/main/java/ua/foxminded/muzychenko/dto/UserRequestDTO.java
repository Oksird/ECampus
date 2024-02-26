package ua.foxminded.muzychenko.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ua.foxminded.muzychenko.dto.profile.UserInfo;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class UserRequestDTO {
    private String id;
    private UserInfo userInfo;
    private RequestTypeDTO requestTypeDTO;
    private RequestStatusDTO requestStatusDTO;
}
