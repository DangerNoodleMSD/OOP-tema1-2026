package structures.types;

import java.util.Optional;
import java.util.TreeMap;

import structures.types.datapoints.DataPoint;

/**
 * The container of datapoints during the day. It contains 96 datapoints, one
 * for each 15 minutes
 * DailyRepo
 */
public final class DailyRepo {
    private static final int INTERVAL_COUNT = 96;
    private TreeMap<Integer, DataPoint> dataPoints;

    public DailyRepo() {
        dataPoints = new TreeMap<>();
        for (int i = 1; i <= INTERVAL_COUNT; i++) {
            dataPoints.put(i, null);
        }
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
}
