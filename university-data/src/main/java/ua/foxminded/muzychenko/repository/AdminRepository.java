package ua.foxminded.muzychenko.repository;

import org.springframework.stereotype.Repository;
import ua.foxminded.muzychenko.entity.Admin;

@Repository
public interface AdminRepository extends UserRepository<Admin> {
}
