package teamproject.bloom.repository.wine;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamproject.bloom.exception.SpecificationNotFoundException;
import teamproject.bloom.model.Wine;
import teamproject.bloom.repository.SpecificationProvider;
import teamproject.bloom.repository.SpecificationProviderManager;

@Component
@RequiredArgsConstructor
public class WineSpecificationProviderManager implements SpecificationProviderManager<Wine> {
    private final List<SpecificationProvider<Wine>> wineSpecificationProviders;

    @Override
    public SpecificationProvider<Wine> getSpecificationProvider(String key) {
        return wineSpecificationProviders
                .stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new SpecificationNotFoundException(
                        "Can`t find correct specification provider of key " + key));
    }
}
