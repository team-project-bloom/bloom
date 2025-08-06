package teamproject.bloom.repository.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamproject.bloom.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserName(String userName);

    @EntityGraph(attributePaths = {"favorites", "favorites.wine"})
    Optional<User> findByUserName(String userName);
}
