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
public class GrapeSpecificationProvider implements SpecificationProvider<Wine> {
    public static final String GRAPE = "grape";

    @Override
    public String getKey() {
        return GRAPE;
    }

    @Override
    public Specification<Wine> getSpecification(Object[] params) {
        List<String> grapes = Arrays.stream(params)
                .map(String::valueOf)
                .map(String::toLowerCase)
                .toList();
        return (root, query, criteriaBuilder) -> {
            Expression<String> grapeExpression = criteriaBuilder
                    .lower(root.get(GRAPE).get("name"));
            return grapeExpression.in(grapes);
        };
    }
}
