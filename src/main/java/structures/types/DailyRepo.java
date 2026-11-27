package structures.types;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.TreeMap;

import structures.templateio.DailyTemplate;
import structures.templateio.TemplatePoint;
import structures.types.datapoints.DataPoint;
import structures.types.enums.DataType;

/**
 * The container of datapoints during the day. It contains 96 datapoints, one
 * for each 15 minutes
 * DailyRepo
 */
public final class DailyRepo {
    private final DataType type;
    private final LocalDate date;
    private final String biddingZoneDomain;
    private final String periodStart;
    private final String periodEnd;
    private final String resolution;
    private final String currencyUnit;
    private final String priceMeasureUnit;
    private final String quantityMeasureUnit;
    private TreeMap<Integer, DataPoint> dataPoints;

    public DailyRepo(final DataType type, final LocalDate date, final DailyTemplate template) {
        dataPoints = new TreeMap<>();
        for (TemplatePoint stub : template.getPoints()) {
            dataPoints.put(stub.getIntervalIndex(), null);
        }
        this.date = date;
        this.periodStart = TimeIntervalUtil.formatTimestamp(date.atStartOfDay());
        this.periodEnd = TimeIntervalUtil.formatTimestamp(
                date.plusDays(1).atStartOfDay().minusMinutes(1));
        this.biddingZoneDomain = template.getBiddingZoneDomain();
        this.resolution = template.getResolution();
        this.currencyUnit = template.getCurrencyUnit();
        this.priceMeasureUnit = template.getPriceMeasureUnit();
        this.quantityMeasureUnit = template.getQuantityMeasureUnit();
        this.type = type;
    }

    /**
     * Scans backwards through all slots of this day and returns the last non-null
     * point.
     */
    public Optional<DataPoint> findLastNonNull() {
        for (DataPoint point : dataPoints.descendingMap().values()) {
            if (point != null) {
                return Optional.of(point);
            }
        }
        return Optional.empty();
    }

    /**
     * Scans backwards from slots strictly before the given interval index and
     * returns
     * the first non-null point found.
     */
    public Optional<DataPoint> findLastNonNullBefore(final int intervalIndex) {
        for (DataPoint point : dataPoints.headMap(intervalIndex, false)
                .descendingMap().values()) {
            if (point != null) {
                return Optional.of(point);
            }
        }
        return Optional.empty();
    }

    /**
     * Adding a DataPoint to the DailyRepo
     *
     * @param point
     *              DataPoint to add
     */
    public void put(final DataPoint point) {
        dataPoints.put(point.getIntervalIndex(), point);
    }

    /**
     * Clear a specific point in the DailyRepo
     *
     * @param intervalIndex
     *                      The interval of the DataPoint to clear
     */
    public void clearSlot(final int intervalIndex) {
        dataPoints.put(intervalIndex, null);
    }

    /**
     * Returns all non null DataPoints in DailyRepo
     */
    public ArrayList<DataPoint> getAllNonNullPoints() {
        ArrayList<DataPoint> list = new ArrayList<>();
        for (DataPoint point : dataPoints.values()) {
            if (point != null) {
                list.add(point);
            }
        }
        return list;
    }

    public DataType getType() {
        return type;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getBiddingZoneDomain() {
        return biddingZoneDomain;
    }

    public String getPeriodStart() {
        return periodStart;
    }

    public String getPeriodEnd() {
        return periodEnd;
    }

    public String getResolution() {
        return resolution;
    }

    public String getCurrencyUnit() {
        return currencyUnit;
    }

    public String getPriceMeasureUnit() {
        return priceMeasureUnit;
    }

    public String getQuantityMeasureUnit() {
        return quantityMeasureUnit;
    }
}
