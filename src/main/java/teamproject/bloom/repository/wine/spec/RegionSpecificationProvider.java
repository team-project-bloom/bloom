package teamproject.bloom.repository.wine.spec;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import teamproject.bloom.model.Wine;
import teamproject.bloom.repository.SpecificationProvider;
import teamproject.bloom.repository.region.RegionRepository;

@Component
@RequiredArgsConstructor
public class RegionSpecificationProvider implements SpecificationProvider<Wine> {
    public static final String REGION = "region";
    private final RegionRepository regionRepository;

    @Override
    public String getKey() {
        return REGION;
    }

    @Override
    public Specification<Wine> getSpecification(Object[] params) {
        Object[] regions = Arrays.stream(params)
                .map(name -> regionRepository.findByName(name.toString()))
                .toArray();
        return (root, query, criteriaBuilder)
                -> root.get(REGION).in(Arrays.asList(regions));
    }
}
