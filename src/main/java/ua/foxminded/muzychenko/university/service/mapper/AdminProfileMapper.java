package ua.foxminded.muzychenko.university.service.mapper;

import ua.foxminded.muzychenko.university.config.Mapper;
import ua.foxminded.muzychenko.university.dto.profile.AdminProfile;
import ua.foxminded.muzychenko.university.entity.Admin;

@Mapper
public class AdminProfileMapper {
    public AdminProfile mapAdminEntityToAdminProfile(Admin admin) {
        return new AdminProfile(admin.getFirstName(), admin.getLastName(), admin.getEmail());
    }
}
