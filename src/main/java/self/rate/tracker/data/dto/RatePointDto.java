package self.rate.tracker.data.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RatePointDto(LocalDate createdAt, BigDecimal value) {

  public static RatePointDto of(LocalDate createdAt, BigDecimal value) {
    return new RatePointDto(createdAt, value);
  }
}
