package teamproject.bloom.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamproject.bloom.dto.favoriteitem.FavoriteItemResponseDto;
import teamproject.bloom.dto.favoriteitem.FavoriteWineRequestDto;

public interface FavoriteItemService {
    FavoriteItemResponseDto addFavoriteItem(FavoriteWineRequestDto requestDto,
                                            String userName);

    Page<FavoriteItemResponseDto> getAllUserFavoriteItems(String userName, Pageable pageable);

    FavoriteItemResponseDto getFavoriteItem(Long wineId, String userName);
}
