package ua.foxminded.muzychenko.service.mapper;

import ua.foxminded.muzychenko.config.Mapper;
import ua.foxminded.muzychenko.dto.profile.StaffProfile;
import ua.foxminded.muzychenko.entity.Staff;

@Mapper
public class StaffProfileMapper {
    public StaffProfile mapStaffEntityToProfile(Staff staff) {
        return new StaffProfile(staff.getUserId().toString(), staff.getFirstName(), staff.getLastName(), staff.getEmail());
    }
}
