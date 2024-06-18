package self.rate.tracker.config;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import self.rate.tracker.config.properties.ProvidersProperties;
import self.rate.tracker.config.properties.ProvidersProperties.ProviderProperties;
import self.rate.tracker.service.adapter.RatesAdapter;
import self.rate.tracker.service.adapter.SimpleListAdapter;
import self.rate.tracker.service.provider.RatesProvider;
import self.rate.tracker.service.provider.SimpleRatesProvider;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(ProvidersProperties.class)
public class ProviderConfiguration {

  private final WebClient webClient;
  private final Map<String, BiFunction<Map<String, RatesAdapter>, ProviderProperties, RatesProvider>> providersBuilders = new HashMap<>();

  @PostConstruct
  public void init() {
    providersBuilders.put("MonoBank", this::buildSimpleRatesProvider);
  }

  @Bean
  public Map<String, RatesProvider> ratesProviders(ProvidersProperties properties,
                                                   Map<String, RatesAdapter> ratesAdapters
  ) {
    return properties.getProviders().stream()
        .collect(Collectors.toMap(
            ProviderProperties::getName,
            p -> Optional.ofNullable(providersBuilders.get(p.getName()))
                .orElseThrow()
                .apply(ratesAdapters, p)));
  }

  @Bean
  public Map<String, RatesAdapter> ratesAdapters(List<RatesAdapter> adapters) {
    return adapters.stream().collect(Collectors.toMap(a -> "", Function.identity()));
  }

  @Bean
  public RatesAdapter simpleListRatesAdapter() {
    return new SimpleListAdapter();
  }

  private RatesProvider buildSimpleRatesProvider(Map<String, RatesAdapter> adapters,
                                                 ProviderProperties properties
  ) {
    RatesAdapter adapter = Optional.ofNullable(adapters.get(properties.getAdapter().getId()))
        .orElseThrow();

    return new SimpleRatesProvider(adapter, webClient, properties);
  }
}
