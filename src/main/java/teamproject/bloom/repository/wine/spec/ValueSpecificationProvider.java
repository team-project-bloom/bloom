package teamproject.bloom.repository.wine.spec;

import io.jsonwebtoken.lang.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import teamproject.bloom.model.Wine;
import teamproject.bloom.repository.SpecificationProvider;

@Component
public class ValueSpecificationProvider implements SpecificationProvider<Wine> {
    public static final String VALUE = "value";

    @Override
    public String getKey() {
        return VALUE;
    }

    @Override
    public Specification<Wine> getSpecification(Object[] params) {
        return ((root, query, criteriaBuilder) ->
                root.get(VALUE).in(Arrays.asList(params)));
    }
}
