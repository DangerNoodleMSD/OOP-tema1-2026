package structures.types;

import java.util.TreeMap;

import fileio.CommandInput;
import structures.types.datapoints.DataPoint;
import structures.types.enums.DataType;

/**
 * The container of datapoints during the day. It contains 96 datapoints, one
 * for each 15 minutes
 * DailyRepo
 */
public class DailyRepo {
    private DataType type;
    private TreeMap<Integer, DataPoint> dataPoints;

    public DailyRepo(DataType type) {
        for (int i = 1; i <= 96; i++) {
            dataPoints.put(i, null);
        }
        this.type = type;
    }

    // TODO: all the functions below
    public void importCmd(CommandInput cmd);

    public void patchCmd(CommandInput cmd);

    public void getCmd(CommandInput cmd);
}
