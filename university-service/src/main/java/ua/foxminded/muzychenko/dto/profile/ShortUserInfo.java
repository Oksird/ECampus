package ua.foxminded.muzychenko.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class ShortUserInfo {
    private String id;
    private String role;
}
