package ua.foxminded.muzychenko.repository;

import org.springframework.stereotype.Repository;
import ua.foxminded.muzychenko.entity.Staff;

@Repository
public interface StaffRepository extends UserRepository<Staff> {
}
