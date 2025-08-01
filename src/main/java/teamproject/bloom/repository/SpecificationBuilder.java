package teamproject.bloom.repository;

import org.springframework.data.jpa.domain.Specification;
import teamproject.bloom.dto.wine.WineSearchParametersDto;

public interface SpecificationBuilder<T> {
    Specification<T> build(WineSearchParametersDto searchParameters);
}
