package self.rate.tracker.config.properties;

import java.util.List;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties("rates")
public class ProvidersProperties {

  private List<ProviderProperties> providers;

  @Getter
  public static class ProviderProperties {

    private String name;
    private String ratesUrl;
    private int cacheExpiresMinutes;
    private AdapterProperties adapter;
  }

  @Getter
  public static class AdapterProperties {

    private String id;
    private CurrencyProperties currencyProperties;
  }

  @Getter
  public static class CurrencyProperties {

    private String baseCode;
    private String counterCode;
    private String bid;
    private String ask;
  }
}
