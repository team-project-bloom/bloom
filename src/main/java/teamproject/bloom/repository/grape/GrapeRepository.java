package teamproject.bloom.repository.grape;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamproject.bloom.model.Grape;

@Repository
public interface GrapeRepository extends JpaRepository<Grape, Long> {
    Grape findByName(String name);
}
