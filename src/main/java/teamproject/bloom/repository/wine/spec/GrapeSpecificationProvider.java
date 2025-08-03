package teamproject.bloom.repository.wine.spec;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import teamproject.bloom.model.Wine;
import teamproject.bloom.repository.SpecificationProvider;
import teamproject.bloom.repository.grape.GrapeRepository;

@Component
@RequiredArgsConstructor
public class GrapeSpecificationProvider implements SpecificationProvider<Wine> {
    public static final String GRAPE = "grape";
    private final GrapeRepository grapeRepository;

    @Override
    public String getKey() {
        return GRAPE;
    }

    @Override
    public Specification<Wine> getSpecification(Object[] params) {
        Object[] grapes = Arrays.stream(params)
                .map(name -> grapeRepository.findByName(name.toString()))
                .toArray();
        return (root, query, criteriaBuilder) ->
                root.get(GRAPE).in(Arrays.asList(grapes));
    }
}
