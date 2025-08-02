package teamproject.bloom.repository.wine.spec;

import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import teamproject.bloom.model.Wine;
import teamproject.bloom.repository.SpecificationProvider;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Wine> {
    public static final String TITLE = "title";

    @Override
    public String getKey() {
        return TITLE;
    }

    @Override
    public Specification<Wine> getSpecification(Object[] params) {
        return (root, query, criteriaBuilder)
                -> root.get(TITLE).in(Arrays.asList(params));
    }
}
