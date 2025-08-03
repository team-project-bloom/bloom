package teamproject.bloom.repository.wine;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import teamproject.bloom.dto.wine.WineSearchParametersDto;
import teamproject.bloom.model.Wine;
import teamproject.bloom.repository.SpecificationBuilder;
import teamproject.bloom.repository.SpecificationProviderManager;
import teamproject.bloom.repository.wine.spec.PriceSpecificationProvider;
import teamproject.bloom.repository.wine.spec.TitleSpecificationProvider;

@Component
@RequiredArgsConstructor
public class WineSpecificationBuilder implements SpecificationBuilder<Wine> {
    private final SpecificationProviderManager<Wine> wineSpecificationProviderManager;

    @Override
    public Specification<Wine> build(WineSearchParametersDto searchParameters) {
        Specification<Wine> spec = Specification.unrestricted();
        if (searchParameters.title() != null && searchParameters.title().length > 0) {
            spec = spec.and(wineSpecificationProviderManager
                    .getSpecificationProvider(TitleSpecificationProvider.TITLE)
                    .getSpecification(searchParameters.title()));
        }
        if (searchParameters.priceFrom() != null || searchParameters.priceTo() != null) {
            spec = spec.and(wineSpecificationProviderManager
                    .getSpecificationProvider(PriceSpecificationProvider.PRICE)
                    .getSpecification(new Object[]{searchParameters.priceFrom(),
                            searchParameters.priceTo()}));
        }
        return spec;
    }
}
