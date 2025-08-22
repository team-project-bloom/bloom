package teamproject.bloom.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static teamproject.bloom.util.FavoriteItemTestUtil.favoriteItem;
import static teamproject.bloom.util.FavoriteItemTestUtil.favoriteWineRequestDto;
import static teamproject.bloom.util.FavoriteItemTestUtil.mapFavoriteItemToDto;
import static teamproject.bloom.util.UserTestUtil.user;
import static teamproject.bloom.util.UserTestUtil.userWithFavorite;
import static teamproject.bloom.util.WineTestUtil.wine;

import jakarta.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import teamproject.bloom.dto.favoriteitem.FavoriteItemResponseDto;
import teamproject.bloom.dto.favoriteitem.FavoriteWineRequestDto;
import teamproject.bloom.exception.unchecked.EntityNotFoundException;
import teamproject.bloom.mapper.FavoriteItemMapper;
import teamproject.bloom.model.FavoriteItem;
import teamproject.bloom.model.User;
import teamproject.bloom.model.Wine;
import teamproject.bloom.repository.favoriteitem.FavoriteItemRepository;
import teamproject.bloom.repository.user.UserRepository;
import teamproject.bloom.repository.wine.WineRepository;
import teamproject.bloom.service.impl.FavoriteItemServiceImpl;

@ExtendWith(MockitoExtension.class)
public class FavoriteItemServiceTest {
    @InjectMocks
    private FavoriteItemServiceImpl favoriteItemService;
    @Mock
    private FavoriteItemRepository favoriteItemRepository;
    @Mock
    private FavoriteItemMapper favoriteItemMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private WineRepository wineRepository;

    @Test
    @DisplayName("Verify method addFavoriteItem with correct data")
    public void addFavoriteItemNonExists_CorrectData_ReturnDto() {
        Wine wine = wine(1L, "Wine");
        User user = user(1L, "userName");
        FavoriteItem favoriteItem = favoriteItem(1L, wine, user);
        FavoriteWineRequestDto favoriteWineRequestDto = favoriteWineRequestDto(wine.getId());
        FavoriteItemResponseDto expected = mapFavoriteItemToDto(favoriteItem);

        when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));
        when(wineRepository.findById(wine.getId())).thenReturn(Optional.of(wine));
        when(favoriteItemMapper.toModel(user, wine)).thenReturn(favoriteItem);
        when(favoriteItemRepository.save(favoriteItem)).thenReturn(favoriteItem);
        when(favoriteItemMapper.toResponseDto(favoriteItem)).thenReturn(expected);
        FavoriteItemResponseDto actual = favoriteItemService.addFavoriteItem(
                favoriteWineRequestDto, user.getUserName());

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Verify method addFavoriteItemIsExists with incorrect data.
             A favorite item is exists
            """)
    public void addFavoriteItemIsExists_IncorrectData_ReturnDto() {
        Wine wine = wine(1L, "Wine");
        Long itemId = 1L;
        User user = userWithFavorite(1L, wine, itemId);
        FavoriteItem favoriteItem = user.getFavorites().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .get();
        FavoriteWineRequestDto favoriteWineRequestDto = favoriteWineRequestDto(wine.getId());

        when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));
        when(wineRepository.findById(wine.getId())).thenReturn(Optional.of(wine));
        when(favoriteItemMapper.toModel(user, wine)).thenReturn(favoriteItem);
        Exception actual = assertThrows(EntityExistsException.class,
                () -> favoriteItemService.addFavoriteItem(
                        favoriteWineRequestDto, user.getUserName()));

        String expected = String.format("Wine from id %s is already a favorite",
                favoriteWineRequestDto.wineId());
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Verify method getAllUserFavoriteItems with correct data")
    public void getAllUserFavoriteItems_CorrectData_ReturnPageDto() {
        User user = user(1L, "userName");
        Wine wine = wine(1L, "Wine");
        FavoriteItem favoriteItem = favoriteItem(1L, wine, user);
        FavoriteItemResponseDto expected = mapFavoriteItemToDto(favoriteItem);
        Pageable pageable = PageRequest.of(0, 10);
        List<FavoriteItem> favoriteItems = List.of(favoriteItem);
        PageImpl<FavoriteItem> pageFavoriteItem = new PageImpl<>(
                favoriteItems, pageable, favoriteItems.size());

        when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));
        when(favoriteItemRepository.findAllByUserId(user.getId(), pageable))
                .thenReturn(pageFavoriteItem);
        when(favoriteItemMapper.toResponseDto(favoriteItem)).thenReturn(expected);
        Page<FavoriteItemResponseDto> pages = favoriteItemService.getAllUserFavoriteItems(
                user.getUserName(), pageable);
        List<FavoriteItemResponseDto> actual = pages.get().toList();

        assertEquals(1, actual.size());
        assertEquals(expected, actual.get(0));
    }

    @Test
    @DisplayName("Verify method deleteFavoriteItem with correct data")
    public void deleteFavoriteItem_CorrectData_Void() {
        Wine wine = wine(1L, "Wine");
        User user = userWithFavorite(1L, wine, 1L);

        when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));
        favoriteItemService.deleteFavoriteItem(1L, user.getUserName());

        verify(userRepository, times(1)).findByUserName(user.getUserName());
    }

    @Test
    @DisplayName("""
            Verify method getFavoriteItem with incorrect data.
             A favorite item is exists
            """)
    public void deleteFavoriteItem_IncorrectData_Void() {
        User user = user(1L, "userName");

        when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));
        Exception actual = assertThrows(EntityNotFoundException.class,
                () -> favoriteItemService.deleteFavoriteItem(1L, user.getUserName())
        );

        String expected = String.format("Can`t find favorite item by id %s from user by id %s",
                1L, user.getId());
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("""
            Verify method getUserByName with incorrect data.
             The user isn`t exists
            """)
    public void getUserByName_IncorrectData_ReturnModel() {
        String userName = "UserName";
        when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());
        Exception actual = assertThrows(EntityNotFoundException.class,
                () -> favoriteItemService.deleteFavoriteItem(1L, userName));

        String expected = "Can`t find user by name " + userName;
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("""
            Verify method getWineById with incorrect data.
             The wine isn`t exists
            """)
    public void getWineById_IncorrectData_ReturnModel() {
        long wineId = 10L;
        User user = user(1L, "userName");
        FavoriteWineRequestDto favoriteWineRequestDto = favoriteWineRequestDto(wineId);

        when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));
        when(wineRepository.findById(wineId)).thenReturn(Optional.empty());
        Exception actual = assertThrows(EntityNotFoundException.class,
                () -> favoriteItemService.addFavoriteItem(
                        favoriteWineRequestDto, user.getUserName()));

        String expected = "Can`t find wine by id " + wineId;
        assertEquals(expected, actual.getMessage());
    }
}
