package self.rate.tracker.service.adapter;

import java.util.List;
import java.util.Map;
import self.rate.tracker.config.properties.ProvidersProperties.AdapterProperties;
import self.rate.tracker.data.dto.RatesHolderDto;
import self.rate.tracker.service.provider.RatesFetcher;

public class SimpleListAdapter implements RatesAdapter {

  @Override
  public RatesHolderDto getRates(RatesFetcher fetcher, AdapterProperties properties) {
    List<Map<String, Object>> rawRates = fetcher.fetch(List.class);
    System.out.println(rawRates);
    return null;
  }
}
