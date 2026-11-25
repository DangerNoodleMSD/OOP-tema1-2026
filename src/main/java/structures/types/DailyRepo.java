package structures.types;

import java.util.TreeMap;

import structures.types.datapoints.DataPoint;
import structures.types.enums.DataType;

/**
 * The container of datapoints during the day. It contains 96 datapoints, one
 * for each 15 minutes
 * DailyRepo
 */
public class DailyRepo {
    private final int intervalCount = 96;
    private DataType type;
    private TreeMap<Integer, DataPoint> dataPoints;

    public DailyRepo(final DataType type) {
        dataPoints = new TreeMap<>();
        for (int i = 1; i <= intervalCount; i++) {
            dataPoints.put(i, null);
        }
        this.type = type;
    }
}
