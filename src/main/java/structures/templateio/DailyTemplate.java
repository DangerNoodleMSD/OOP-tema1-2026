package structures.templateio;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public final class DailyTemplate {
    private String biddingZoneDomain;
    private String periodStart;
    private String periodEnd;
    private String resolution;
    private String currencyUnit;
    private String priceMeasureUnit;
    private String quantityMeasureUnit;
    private List<TemplatePoint> points;
}
