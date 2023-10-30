package architecture.user;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, UUID> {
    boolean existsByEmailOrUsername(String email, String username);

    @Query(value = "{$and: [{'_id': {$ne: ?0}}, {$or: [{'email': ?1}, {'username': ?2}]}]}", exists = true)
    boolean existsByEmailOrUsername(UUID id, String email, String username);

    Optional<User> findByUsername(String username);
}
