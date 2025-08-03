package teamproject.bloom.repository.wine.spec;

import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import teamproject.bloom.model.Wine;
import teamproject.bloom.repository.SpecificationProvider;

@Component
public class VarietySpecificationProvider implements SpecificationProvider<Wine> {
    public static final String VARIETY = "variety";

    @Override
    public String getKey() {
        return VARIETY;
    }

    @Override
    public Specification<Wine> getSpecification(Object[] params) {
        return ((root, query, criteriaBuilder)
                -> root.get(VARIETY).in(Arrays.asList(params)));
    }
}
