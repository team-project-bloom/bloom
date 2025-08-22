package teamproject.bloom.repository.favoriteitem;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamproject.bloom.model.FavoriteItem;

@Repository
public interface FavoriteItemRepository extends JpaRepository<FavoriteItem, Long> {
    Page<FavoriteItem> findAllByUserId(Long userId, Pageable pageable);

    Optional<FavoriteItem> findByUserIdAndWineId(Long userId, Long wineId);
}
