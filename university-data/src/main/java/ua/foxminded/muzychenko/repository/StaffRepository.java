package ua.foxminded.muzychenko.repository;

import org.springframework.stereotype.Repository;
import ua.foxminded.muzychenko.entity.Staff;

import java.util.Optional;

@Repository
public interface StaffRepository extends UserRepository<Staff> {
    Optional<Staff> findByEmail(String email);
}
