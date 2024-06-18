package self.rate.tracker.service.adapter;

import self.rate.tracker.config.properties.ProvidersProperties.AdapterProperties;
import self.rate.tracker.data.dto.RatesHolderDto;
import self.rate.tracker.service.provider.RatesFetcher;

public interface RatesAdapter {

  RatesHolderDto getRates(RatesFetcher fetcher, AdapterProperties properties);
}
