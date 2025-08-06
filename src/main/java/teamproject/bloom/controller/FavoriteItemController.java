package teamproject.bloom.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import teamproject.bloom.dto.favoriteitem.FavoriteItemResponseDto;
import teamproject.bloom.dto.favoriteitem.FavoriteWineRequestDto;
import teamproject.bloom.service.FavoriteItemService;
import teamproject.bloom.service.UserService;

@Tag(name = "FavoriteItem", description = "Endpoints for favorite item management")
@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
public class FavoriteItemController {
    private final FavoriteItemService favoriteItemService;
    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/add")
    @Operation(summary = "Add a favorite wine", description = "Add a favorite wine")
    public FavoriteItemResponseDto addFavoriteItem(
            @RequestBody @Valid FavoriteWineRequestDto requestDto,
            Authentication authentication) {
        return favoriteItemService.addFavoriteItem(requestDto,
                userService.getUserName(authentication));
    }

    @GetMapping("/all")
    public Page<FavoriteItemResponseDto> getUserFavoriteItem(Authentication authentication,
                                                             Pageable pageable) {
        return favoriteItemService.getUserFavoriteItem(
                userService.getUserName(authentication), pageable);
    }
}
