package self.rate.tracker.data.dto;

public record ConversionDto(String bankId, int primaryCode, int secondaryCode) {

  public static ConversionDto of(String bankId, int primaryCode, int secondaryCode) {
    return new ConversionDto(bankId, primaryCode, secondaryCode);
  }
}
