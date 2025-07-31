package teamproject.bloom.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import teamproject.bloom.dto.cartitem.CartItemRequestDto;
import teamproject.bloom.dto.shoppingcart.ShoppingCartDto;
import teamproject.bloom.repository.UserRepository;
import teamproject.bloom.service.ShoppingCartService;

@Tag(name = "ShoppingCart", description = "Endpoints for CRUD operations with shopping cart")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final UserRepository userRepository;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Add item", description = "Add the item to shopping cart")
    public ShoppingCartDto addItem(@RequestBody @Valid CartItemRequestDto addCartItemDto,
                                   Authentication authentication) {
        return shoppingCartService.addItem(addCartItemDto, getUserName(authentication));
    }

    private String getUserName(Authentication authentication) {
        return (String) authentication.getPrincipal();
    }
}
