package self.rate.tracker.service.provider;

import java.util.Set;
import org.springframework.data.util.Pair;

public interface RatesProvider {

  Set<Integer> getAllAvailableCurrencies();

  Set<Integer> getAllAvailableCounterCurrenciesFor(Integer currency);

  Pair<Double, Double> getRatesFor(Integer baseCurrency, Integer counterCurrency);
}
