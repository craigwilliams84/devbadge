package uk.co.craigcodes.devbadge.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uk.co.craigcodes.devbadge.model.user.User;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByReconcileCode(String reconcileCode);
}
