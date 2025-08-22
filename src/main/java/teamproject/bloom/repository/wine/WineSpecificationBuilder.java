package teamproject.bloom.repository.wine;

import static teamproject.bloom.repository.wine.spec.AlcoholSpecificationProvider.ALCOHOL;
import static teamproject.bloom.repository.wine.spec.GrapeSpecificationProvider.GRAPE;
import static teamproject.bloom.repository.wine.spec.PriceSpecificationProvider.PRICE;
import static teamproject.bloom.repository.wine.spec.RegionSpecificationProvider.REGION;
import static teamproject.bloom.repository.wine.spec.TitleSpecificationProvider.TITLE;
import static teamproject.bloom.repository.wine.spec.ValueSpecificationProvider.VALUE;
import static teamproject.bloom.repository.wine.spec.VarietySpecificationProvider.VARIETY;
import static teamproject.bloom.repository.wine.spec.VintageSpecificationProvider.VINTAGE;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import teamproject.bloom.dto.wine.WineSearchParametersDto;
import teamproject.bloom.model.Wine;
import teamproject.bloom.repository.SpecificationBuilder;
import teamproject.bloom.repository.SpecificationProviderManager;

@Component
@RequiredArgsConstructor
public class WineSpecificationBuilder implements SpecificationBuilder<Wine> {
    private final SpecificationProviderManager<Wine> wineSpecificationProviderManager;

    @Override
    public Specification<Wine> build(WineSearchParametersDto params) {
        Specification<Wine> spec = Specification.where(null);
        if (params.title() != null && params.title().length > 0) {
            spec = spec.and(wineSpecificationProviderManager
                    .getSpecificationProvider(TITLE)
                    .getSpecification(params.title()));
        }
        if (params.priceFrom() != null || params.priceTo() != null) {
            spec = spec.and(wineSpecificationProviderManager
                    .getSpecificationProvider(PRICE)
                    .getSpecification(new Object[]{params.priceFrom(),
                            params.priceTo()}));
        }
        if (params.vintageFrom() != null || params.vintageTo() != null) {
            spec = spec.and(wineSpecificationProviderManager
                    .getSpecificationProvider(VINTAGE)
                    .getSpecification(new Object[]{params.vintageFrom(),
                            params.vintageTo()}));
        }
        if (params.alcohol() != null && params.alcohol().length > 0) {
            spec = spec.and(wineSpecificationProviderManager
                    .getSpecificationProvider(ALCOHOL)
                    .getSpecification(params.alcohol()));
        }
        if (params.variety() != null && params.variety().length > 0) {
            spec = spec.and(wineSpecificationProviderManager
                    .getSpecificationProvider(VARIETY)
                    .getSpecification(params.variety()));
        }
        if (params.value() != null && params.value().length > 0) {
            spec = spec.and(wineSpecificationProviderManager
                    .getSpecificationProvider(VALUE)
                    .getSpecification(params.value()));
        }
        if (params.grape() != null && params.grape().length > 0) {
            spec = spec.and(wineSpecificationProviderManager
                    .getSpecificationProvider(GRAPE)
                    .getSpecification(params.grape()));
        }
        if (params.region() != null && params.region().length > 0) {
            spec = spec.and(wineSpecificationProviderManager
                    .getSpecificationProvider(REGION)
                    .getSpecification(params.region()));
        }
        return spec;
    }
}
