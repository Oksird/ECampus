package ua.foxminded.muzychenko.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.muzychenko.entity.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository<E extends User> extends JpaRepository<E, UUID> {
    Optional<E> findByEmail(String email);
}
