package teamproject.bloom.repository.wine.spec;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.stream.Stream;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import teamproject.bloom.model.Wine;
import teamproject.bloom.repository.SpecificationProvider;

@Component
public class PriceSpecificationProvider implements SpecificationProvider<Wine> {
    public static final String PRICE = "price";

    @Override
    public String getKey() {
        return PRICE;
    }

    @Override
    public Specification<Wine> getSpecification(Object[] params) {
        BigDecimal from = (BigDecimal) params[0];
        BigDecimal to = (BigDecimal) params[1];
        return (root, query, cb) -> Stream.of(
                        from != null && to != null
                                ? cb.between(root.get(PRICE), from, to) : null,
                        from != null && to == null
                                ? cb.greaterThanOrEqualTo(root.get(PRICE), from) : null,
                        from == null && to != null
                                ? cb.lessThanOrEqualTo(root.get(PRICE), to) : null
                )
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(cb.conjunction());
    }
}
