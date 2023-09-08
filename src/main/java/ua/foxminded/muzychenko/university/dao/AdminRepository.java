package ua.foxminded.muzychenko.university.dao;

import org.springframework.stereotype.Repository;
import ua.foxminded.muzychenko.university.entity.Admin;

@Repository
public interface AdminRepository extends UserRepository<Admin> {
}
