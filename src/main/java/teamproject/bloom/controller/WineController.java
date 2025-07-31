package teamproject.bloom.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teamproject.bloom.dto.wine.WineResponseDto;
import teamproject.bloom.dto.wine.WineWithAllParamsDto;
import teamproject.bloom.service.WineService;

@Tag(name = "Wines", description = "Endpoints for wine management")
@RestController
@RequestMapping("/wines")
@RequiredArgsConstructor
public class WineController {
    private final WineService wineService;

    @GetMapping
    @Operation(summary = "Get all wines", description = "Get list of all wines with Pageable")
    public Page<WineResponseDto> getAll(Pageable pageable) {
        return wineService.getAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a wine", description = "Get a wine by id")
    public WineWithAllParamsDto getById(@PathVariable Long id) {
        return wineService.getWineById(id);
    }
}
