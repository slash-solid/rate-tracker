package self.rate.tracker.service.provider;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.web.reactive.function.client.WebClient;
import self.rate.tracker.config.properties.ProvidersProperties.ProviderProperties;
import self.rate.tracker.data.dto.RatesHolderDto;
import self.rate.tracker.service.adapter.RatesAdapter;

@RequiredArgsConstructor
public class SimpleRatesProvider implements RatesProvider, RatesFetcher {

  private final RatesAdapter adapter;
  private final WebClient webClient;
  private final ProviderProperties properties;

  private volatile Map<String, Pair<Double, Double>> cachedPairs = null;
  private volatile Map<Integer, Set<Integer>> counterCurrencies = null;
  private volatile Set<Integer> supportedCurrencies = null;
  private RatesHolderDto rates;
  private long cachedAt = 0L;

  @Override
  public Set<Integer> getAllAvailableCurrencies() {
    if (supportedCurrencies == null) {
      synchronized (this) {
        if (supportedCurrencies == null) {
          Set<Integer> currencies = new HashSet<>();
          getRates().rates().forEach((code, pair) -> {
            currencies.add(code);
            currencies.add(pair.getFirst());
          });
          supportedCurrencies = currencies;
        }
      }
    }

    return supportedCurrencies;
  }

  @Override
  public Set<Integer> getAllAvailableCounterCurrenciesFor(Integer currency) {
    if (counterCurrencies == null) {
      synchronized (this) {
        if (counterCurrencies == null) {
          counterCurrencies = new HashMap<>();
        }
      }
    }

    Set<Integer> currencies = counterCurrencies.get(currency);
    if (currencies == null) {
      currencies = getRates().rates().entrySet().stream()
          .filter(e -> e.getKey().equals(currency) || e.getValue().getFirst().equals(currency))
          .map(e -> e.getKey().equals(currency) ? e.getValue().getFirst() : e.getKey())
          .collect(Collectors.toSet());
      counterCurrencies.put(currency, currencies);
    }

    return currencies;
  }

  @Override
  public Pair<Double, Double> getRatesFor(Integer baseCurrency, Integer counterCurrency) {
    if (cachedPairs == null) {
      synchronized (this) {
        if (cachedPairs == null) {
          cachedPairs = new HashMap<>();
        }
      }
    }

    if (!getAllAvailableCurrencies().contains(baseCurrency)
        || !getAllAvailableCurrencies().contains(baseCurrency)) {
      throw new NoSuchElementException("Currency pair is not supported");
    }

    Pair<Double, Double> pair = cachedPairs.get(getPairKey(baseCurrency, counterCurrency));
    if (pair == null) {
      pair = getRates().rates().entrySet().stream()
          .filter(e -> {
            Integer base = e.getKey();
            Integer counter = e.getValue().getFirst();

            return base.equals(baseCurrency) && counter.equals(counterCurrency)
                || base.equals(counterCurrency) && counter.equals(baseCurrency);
          })
          .map(e -> e.getKey().equals(baseCurrency)
              ? Pair.of(1 / e.getValue().getSecond().getSecond(), 1 / e.getValue().getSecond().getFirst())
              : Pair.of(e.getValue().getSecond().getFirst(), e.getValue().getSecond().getSecond()))
          .findAny()
          .orElseThrow();

      cachedPairs.put(getPairKey(baseCurrency, counterCurrency), pair);
    }

    return pair;
  }

  @Override
  public <T> T fetch(Class<T> type) {
    return Objects.requireNonNull(
            webClient.get()
                .uri(properties.getRatesUrl())
                .retrieve()
                .toEntity(type)
                .block())
        .getBody();
  }

  private RatesHolderDto getRates() {
    final long now = Instant.now().getEpochSecond();
    final long threshold = cachedAt + (properties.getCacheExpiresMinutes() * 60L);

    if (rates == null || now > threshold) {
      synchronized (this) {
        if (rates == null || now > threshold) {
          rates = adapter.getRates(this, properties.getAdapter());
          cachedAt = now;
          supportedCurrencies = null;
          counterCurrencies = null;
          cachedPairs = null;
        }
      }
    }

    return rates;
  }

  private String getPairKey(Integer baseCurrency, Integer counterCurrency) {
    return baseCurrency + "-" + counterCurrency;
  }
}
