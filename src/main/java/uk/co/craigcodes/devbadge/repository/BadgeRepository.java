package uk.co.craigcodes.devbadge.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uk.co.craigcodes.devbadge.model.badge.Badge;

import java.util.List;

@Repository
public interface BadgeRepository extends MongoRepository<Badge, String> {

    List<Badge> findByOwnerId(String ownerId);
}
