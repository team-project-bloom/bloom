package teamproject.bloom.repository.wine.spec;

import jakarta.persistence.criteria.Predicate;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import teamproject.bloom.model.Wine;
import teamproject.bloom.repository.SpecificationProvider;

@Component
@RequiredArgsConstructor
public class TitleSpecificationProvider implements SpecificationProvider<Wine> {
    public static final String TITLE = "title";

    @Override
    public String getKey() {
        return TITLE;
    }

    @Override
    public Specification<Wine> getSpecification(Object[] params) {
        List<String> titles = Arrays.stream(params)
                .map(String::valueOf)
                .map(String::toLowerCase)
                .toList();
        return (root, query, criteriaBuilder)
                -> {
            Predicate[] predicates = titles.stream()
                    .map(title -> criteriaBuilder.like(
                            criteriaBuilder.lower(root.get(TITLE)),
                            "%" + title + "%"
                    ))
                    .toArray(Predicate[]::new);
            return criteriaBuilder.or(predicates);
        };
    }
}
