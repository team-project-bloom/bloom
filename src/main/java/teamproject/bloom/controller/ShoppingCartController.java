package teamproject.bloom.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import teamproject.bloom.dto.cartitem.CartItemRequestDto;
import teamproject.bloom.dto.cartitem.CartItemUpdateDto;
import teamproject.bloom.dto.shoppingcart.ShoppingCartResponseDto;
import teamproject.bloom.service.ShoppingCartService;
import teamproject.bloom.service.UserService;

@Tag(name = "ShoppingCart", description = "Endpoints for CRUD operations with shopping cart")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Add CartItem", description = "Add the CartItem into shopping cart")
    public ShoppingCartResponseDto addItem(@RequestBody @Valid CartItemRequestDto addCartItemDto,
                                           Authentication authentication) {
        return shoppingCartService.addItem(addCartItemDto,
                userService.getUserName(authentication));
    }

    @GetMapping
    @Operation(summary = "Get all CartItem",
            description = "Get all CartItem from shopping cart with Pageable")
    public ShoppingCartResponseDto getAllItems(Authentication authentication, Pageable pageable) {
        return shoppingCartService.getAllImages(userService.getUserName(authentication), pageable);
    }

    @PutMapping("/items/{itemId}")
    @Operation(summary = "Update CartItem",
            description = "Update the quantity CartItem into shopping cart")
    public ShoppingCartResponseDto updateCartItem(@RequestBody @Valid CartItemUpdateDto updateDto,
                                                  @PathVariable Long itemId,
                                                  Authentication authentication) {
        return shoppingCartService.updateCartItem(updateDto, itemId,
                userService.getUserName(authentication));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{itemId}")
    @Operation(summary = "Delete CartItem", description = "Delete the CartItem into shopping cart")
    public void deleteCartItem(@PathVariable Long itemId, Authentication authentication) {
        shoppingCartService.deleteCartItem(itemId, userService.getUserName(authentication));
    }
}
