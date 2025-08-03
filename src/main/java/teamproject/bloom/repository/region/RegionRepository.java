package teamproject.bloom.repository.region;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamproject.bloom.model.Region;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    Optional<Region> findByNameIgnoreCase(String name);
}
