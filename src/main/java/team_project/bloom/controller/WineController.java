package team_project.bloom.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team_project.bloom.dto.WineResponseDto;

@Tag(name = "Wines", description = "Endpoints for wine management")
@RestController
@RequestMapping("/wines")
public class WineController {
    @GetMapping
    public WineResponseDto get(Authentication authentication) {
        return new WineResponseDto("Good Wine!!!");
    }
}
