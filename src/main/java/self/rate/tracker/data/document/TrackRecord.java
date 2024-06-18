package self.rate.tracker.data.document;

import java.util.List;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import self.rate.tracker.data.dto.ConversionDto;

@Data
@Document("track_record")
public class TrackRecord {

  private String id;
  private String name;
  private int ratePoints;
  private int rateAverage;
  private List<ConversionDto> conversions;
}
