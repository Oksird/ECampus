package ua.foxminded.muzychenko.repository;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.entity.PendingUser;

import java.util.Optional;

@Repository
public interface PendingUserRepository extends UserRepository<PendingUser> {
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void delete(@NonNull PendingUser entity);
}
