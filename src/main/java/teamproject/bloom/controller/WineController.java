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
import teamproject.bloom.dto.wine.WineResponseWithAllParamsDto;
import teamproject.bloom.dto.wine.WineSearchParametersDto;
import teamproject.bloom.service.WineService;

@Tag(name = "Wine", description = "Endpoints for wine management")
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

    @GetMapping("/{wineId}")
    @Operation(summary = "Get a wine", description = "Get a wine by id")
    public WineResponseWithAllParamsDto getById(@PathVariable Long wineId) {
        return wineService.getWineById(wineId);
    }

    @GetMapping("/search")
    @Operation(summary = "Search wines", description = "Search wines by params")
    public Page<WineResponseDto> search(WineSearchParametersDto wineSearchDto, Pageable pageable) {
        return wineService.search(wineSearchDto, pageable);
    }
}
