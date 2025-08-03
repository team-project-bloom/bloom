package teamproject.bloom.repository.wine.spec;

import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import teamproject.bloom.model.Wine;
import teamproject.bloom.repository.SpecificationProvider;

@Component
public class AlcoholSpecificationProvider implements SpecificationProvider<Wine> {
    public static final String ALCOHOL = "alcohol";

    @Override
    public String getKey() {
        return ALCOHOL;
    }

    @Override
    public Specification<Wine> getSpecification(Object[] params) {
        return ((root, query, criteriaBuilder)
                -> root.get(ALCOHOL).in(Arrays.asList(params)));
    }
}
