package teamproject.bloom.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamproject.bloom.model.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Page<CartItem> findAllByShoppingCartId(Long id, Pageable pageable);

    Optional<CartItem> findByIdAndShoppingCartId(Long itemId, Long cartId);
}
