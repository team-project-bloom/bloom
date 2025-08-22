package teamproject.bloom.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import teamproject.bloom.dto.user.UserLoginResponseDto;
import teamproject.bloom.service.UserService;

@Tag(name = "Auth", description = "Endpoints for register users if they do not exist")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/registration")
    @Operation(summary = "Register a user",
            description = "Automatically register users if they do not exist")
    public UserLoginResponseDto register(
            Authentication authentication,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION,
                    required = false) String authHeader) {
        return userService.register(userService.getUserName(authentication), authHeader);
    }
}
