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
import teamproject.bloom.dto.cartitem.CartItemDto;
import teamproject.bloom.dto.cartitem.CartItemRequestDto;
import teamproject.bloom.dto.shoppingcart.ShoppingCartResponseDto;
import teamproject.bloom.service.ShoppingCartService;

@Tag(name = "ShoppingCart", description = "Endpoints for CRUD operations with shopping cart")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Add item", description = "Add the item to shopping cart")
    public ShoppingCartResponseDto addItem(@RequestBody @Valid CartItemRequestDto addCartItemDto,
                                           Authentication authentication) {
        return shoppingCartService.addItem(addCartItemDto, getUserName(authentication));
    }

    @GetMapping
    @Operation(summary = "Get all items",
            description = "Get all items from shopping cart with Pageable")
    public Page<CartItemDto> getAllItems(Authentication authentication, Pageable pageable) {
        return shoppingCartService.getAllImages(getUserName(authentication), pageable);
    }

    private String getUserName(Authentication authentication) {
        return (String) authentication.getPrincipal();
    }
}
