package self.rate.tracker.service.provider;

public interface RatesFetcher {

  <T> T fetch(Class<T> type);
}
