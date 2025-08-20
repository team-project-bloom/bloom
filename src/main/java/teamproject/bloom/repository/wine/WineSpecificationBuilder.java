package teamproject.bloom.repository.wine;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import teamproject.bloom.dto.wine.WineSearchParametersDto;
import teamproject.bloom.model.Wine;
import teamproject.bloom.repository.SpecificationBuilder;
import teamproject.bloom.repository.SpecificationProviderManager;
import teamproject.bloom.repository.wine.spec.AlcoholSpecificationProvider;
import teamproject.bloom.repository.wine.spec.GrapeSpecificationProvider;
import teamproject.bloom.repository.wine.spec.PriceSpecificationProvider;
import teamproject.bloom.repository.wine.spec.RegionSpecificationProvider;
import teamproject.bloom.repository.wine.spec.TitleSpecificationProvider;
import teamproject.bloom.repository.wine.spec.ValueSpecificationProvider;
import teamproject.bloom.repository.wine.spec.VarietySpecificationProvider;
import teamproject.bloom.repository.wine.spec.VintageSpecificationProvider;

@Component
@RequiredArgsConstructor
public class WineSpecificationBuilder implements SpecificationBuilder<Wine> {
    private final SpecificationProviderManager<Wine> wineSpecificationProviderManager;

    @Override
    public Specification<Wine> build(WineSearchParametersDto params) {
        Specification<Wine> spec = Specification.where(null);
        if (params.title() != null && params.title().length > 0) {
            spec = spec.and(wineSpecificationProviderManager
                    .getSpecificationProvider(TitleSpecificationProvider.TITLE)
                    .getSpecification(params.title()));
        }
        if (params.priceFrom() != null || params.priceTo() != null) {
            spec = spec.and(wineSpecificationProviderManager
                    .getSpecificationProvider(PriceSpecificationProvider.PRICE)
                    .getSpecification(new Object[]{params.priceFrom(),
                            params.priceTo()}));
        }
        if (params.vintageFrom() != null || params.vintageTo() != null) {
            spec = spec.and(wineSpecificationProviderManager
                    .getSpecificationProvider(VintageSpecificationProvider.VINTAGE)
                    .getSpecification(new Object[]{params.vintageFrom(),
                            params.vintageTo()}));
        }
        if (params.alcohol() != null && params.alcohol().length > 0) {
            spec = spec.and(wineSpecificationProviderManager
                    .getSpecificationProvider(AlcoholSpecificationProvider.ALCOHOL)
                    .getSpecification(params.alcohol()));
        }
        if (params.variety() != null && params.variety().length > 0) {
            spec = spec.and(wineSpecificationProviderManager
                    .getSpecificationProvider(VarietySpecificationProvider.VARIETY)
                    .getSpecification(params.variety()));
        }
        if (params.value() != null && params.value().length > 0) {
            spec = spec.and(wineSpecificationProviderManager
                    .getSpecificationProvider(ValueSpecificationProvider.VALUE)
                    .getSpecification(params.value()));
        }
        if (params.grape() != null && params.grape().length > 0) {
            spec = spec.and(wineSpecificationProviderManager
                    .getSpecificationProvider(GrapeSpecificationProvider.GRAPE)
                    .getSpecification(params.grape()));
        }
        if (params.region() != null && params.region().length > 0) {
            spec = spec.and(wineSpecificationProviderManager
                    .getSpecificationProvider(RegionSpecificationProvider.REGION)
                    .getSpecification(params.region()));
        }
        return spec;
    }
}
