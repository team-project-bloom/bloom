package teamproject.bloom.repository.wine.spec;

import jakarta.persistence.criteria.Expression;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import teamproject.bloom.model.Wine;
import teamproject.bloom.repository.SpecificationProvider;

@Component
@RequiredArgsConstructor
public class RegionSpecificationProvider implements SpecificationProvider<Wine> {
    public static final String REGION = "region";

    @Override
    public String getKey() {
        return REGION;
    }

    @Override
    public Specification<Wine> getSpecification(Object[] params) {
        List<String> regions = Arrays.stream(params)
                .map(String::valueOf)
                .map(String::toLowerCase)
                .toList();
        return (root, query, criteriaBuilder)
                -> {
            Expression<String> regionExpression = criteriaBuilder
                    .lower(root.get(REGION).get("name"));
            return regionExpression.in(regions);
        };
    }
}
