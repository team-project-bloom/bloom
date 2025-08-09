package teamproject.bloom.dto.favoriteitem;

public record FavoriteItemResponseDto(
        Long id,
        Long userId,
        Long wineId,
        String region,
        String grape
) {
}
