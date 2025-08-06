package teamproject.bloom.service;

import teamproject.bloom.dto.favoriteitem.FavoriteItemResponseDto;
import teamproject.bloom.dto.favoriteitem.FavoriteWineRequestDto;

public interface FavoriteItemService {
    FavoriteItemResponseDto addFavoriteItem(FavoriteWineRequestDto requestDto,
                                            String userName);
}
