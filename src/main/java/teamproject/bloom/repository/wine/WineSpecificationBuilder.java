package teamproject.bloom.repository.wine;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import teamproject.bloom.dto.wine.WineSearchParametersDto;
import teamproject.bloom.model.Wine;
import teamproject.bloom.repository.SpecificationBuilder;

@Component
public class WineSpecificationBuilder implements SpecificationBuilder<Wine> {

    @Override
    public Specification<Wine> build(WineSearchParametersDto searchParameters) {
        return null;
    }
}
