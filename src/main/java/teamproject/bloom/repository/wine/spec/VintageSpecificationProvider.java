package teamproject.bloom.repository.wine.spec;

import java.util.Objects;
import java.util.stream.Stream;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import teamproject.bloom.model.Wine;
import teamproject.bloom.repository.SpecificationProvider;

@Component
public class VintageSpecificationProvider implements SpecificationProvider<Wine> {
    public static final String VINTAGE = "vintage";

    @Override
    public String getKey() {
        return VINTAGE;
    }

    @Override
    public Specification<Wine> getSpecification(Object[] params) {
        Integer from = (Integer) params[0];
        Integer to = (Integer) params[1];
        return ((root, query, cb) -> Stream.of(
                from != null && to != null
                        ? cb.between(root.get(VINTAGE), from, to) : null,
                from != null && to == null
                        ? cb.greaterThanOrEqualTo(root.get(VINTAGE), from) : null,
                from == null && to != null
                ? cb.lessThanOrEqualTo(root.get(VINTAGE), to) : null
                )
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(cb.conjunction()));
    }
}
