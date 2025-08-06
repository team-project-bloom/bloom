package teamproject.bloom.service.impl;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import teamproject.bloom.dto.favoriteitem.FavoriteItemResponseDto;
import teamproject.bloom.dto.favoriteitem.FavoriteWineRequestDto;
import teamproject.bloom.exception.EntityNotFoundException;
import teamproject.bloom.mapper.FavoriteItemMapper;
import teamproject.bloom.model.FavoriteItem;
import teamproject.bloom.model.User;
import teamproject.bloom.model.Wine;
import teamproject.bloom.repository.favoriteitem.FavoriteItemRepository;
import teamproject.bloom.repository.user.UserRepository;
import teamproject.bloom.repository.wine.WineRepository;
import teamproject.bloom.service.FavoriteItemService;

@Service
@RequiredArgsConstructor
public class FavoriteItemServiceImpl implements FavoriteItemService {
    private final FavoriteItemRepository favoriteItemRepository;
    private final FavoriteItemMapper favoriteItemMapper;
    private final UserRepository userRepository;
    private final WineRepository wineRepository;

    @Override
    public FavoriteItemResponseDto addFavoriteItem(
            FavoriteWineRequestDto requestDto, String userName) {
        User user = getUserByName(userName);
        Wine wine = getWineById(requestDto.id());
        FavoriteItem favoriteItem = favoriteItemMapper.toModel(user, wine);
        user.getFavorites()
                .stream()
                .filter(i -> i.getWine().getId().equals(requestDto.id()))
                .findFirst()
                .ifPresent(i -> {
                    throw new EntityExistsException(
                            String.format("Wine from id %s is already a favorite", requestDto.id())
                    );
                });
        return favoriteItemMapper.toResponseDto(favoriteItemRepository.save(favoriteItem));
    }

    @Override
    public Page<FavoriteItemResponseDto> getAllUserFavoriteItems(
            String userName, Pageable pageable) {
        User user = getUserByName(userName);
        return favoriteItemRepository.findAllByUserId(user.getId(), pageable)
                .map(favoriteItemMapper::toResponseDto);
    }

    @Override
    public FavoriteItemResponseDto getFavoriteItem(Long wineId, String userName) {
        User user = getUserByName(userName);
        FavoriteItem favoriteItem = favoriteItemRepository.findByUserIdAndWineId(
                        user.getId(), wineId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Can`t find favorite item by wine id "
                                + wineId)
                );
        return favoriteItemMapper.toResponseDto(favoriteItem);
    }

    private User getUserByName(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(
                () -> new EntityNotFoundException("Can`t find user by name " + userName)
        );
    }

    private Wine getWineById(Long id) {
        return wineRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can`t find wine by id " + id)
        );
    }
}
