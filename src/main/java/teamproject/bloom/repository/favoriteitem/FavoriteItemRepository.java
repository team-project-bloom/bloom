package teamproject.bloom.repository.favoriteitem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamproject.bloom.model.FavoriteItem;

@Repository
public interface FavoriteItemRepository extends JpaRepository<FavoriteItem, Long> {
}
