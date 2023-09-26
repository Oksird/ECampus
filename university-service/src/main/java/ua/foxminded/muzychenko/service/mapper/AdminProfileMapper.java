package ua.foxminded.muzychenko.service.mapper;

import ua.foxminded.muzychenko.config.Mapper;
import ua.foxminded.muzychenko.dto.profile.AdminProfile;
import ua.foxminded.muzychenko.entity.Admin;

@Mapper
public class AdminProfileMapper {
    public AdminProfile mapAdminEntityToAdminProfile(Admin admin) {
        return new AdminProfile(admin.getUserId().toString() ,admin.getFirstName(), admin.getLastName(), admin.getEmail());
    }
}
