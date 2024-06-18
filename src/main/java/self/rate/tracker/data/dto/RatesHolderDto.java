package self.rate.tracker.data.dto;

import java.util.Map;
import org.springframework.data.util.Pair;

public record RatesHolderDto(Map<Integer, Pair<Integer, Pair<Double, Double>>> rates) {

  public static RatesHolderDto of(Map<Integer, Pair<Integer, Pair<Double, Double>>> rates) {
    return new RatesHolderDto(rates);
  }
}
