package self.rate.tracker.data.document;

import java.util.List;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import self.rate.tracker.data.dto.RatePointDto;

@Data
@Document("rate_ledger")
public class RateLedger {

  private String id;
  private String trackRecordId;
  private List<RatePointDto> ratePoints;
}
