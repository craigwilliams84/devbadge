package uk.co.craigcodes.devbadge.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uk.co.craigcodes.devbadge.model.badge.Badge;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface BadgeRepository extends MongoRepository<Badge, String> {

    List<Badge> findByOwnerId(String ownerId);

    Optional<Badge> findByTypeIdAndNftDetailsOwnerAddressAndNftDetailsMetadataUrl(
            BigInteger typeId, String ownerId, String metadataUrl);
}
