package teamproject.bloom.repository.wine;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import teamproject.bloom.dto.wine.WineSearchParametersDto;
import teamproject.bloom.model.Wine;
import teamproject.bloom.repository.SpecificationBuilder;
import teamproject.bloom.repository.SpecificationProviderManager;
import teamproject.bloom.repository.wine.spec.AlcoholSpecificationProvider;
import teamproject.bloom.repository.wine.spec.PriceSpecificationProvider;
import teamproject.bloom.repository.wine.spec.TitleSpecificationProvider;

@Component
@RequiredArgsConstructor
public class WineSpecificationBuilder implements SpecificationBuilder<Wine> {
    private final SpecificationProviderManager<Wine> wineSpecificationProviderManager;

    @Override
    public Specification<Wine> build(WineSearchParametersDto params) {
        Specification<Wine> spec = Specification.unrestricted();
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
        if (params.alcohol() != null && params.alcohol().length > 0) {
            spec = spec.and(wineSpecificationProviderManager
                    .getSpecificationProvider(AlcoholSpecificationProvider.ALCOHOL)
                    .getSpecification(params.alcohol()));
        }
        return spec;
    }
}
