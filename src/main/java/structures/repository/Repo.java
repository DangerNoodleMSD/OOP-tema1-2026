package structures.repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import structures.types.DailyRepo;

enum DataType {
    DAMPrices,
    DAMPricesPrediction,
    GridLoadPrediction,
    GridLoad,
    ImbalancePrices,
    ImbalanceVolumes
}

public final class Repo {
    private boolean importing;
    private static Repo repo = null;

    private Repo() {
    }

    private Map<DataType, TreeMap<LocalDate, DailyRepo>> data;

    /**
     * Returns a new repository if there is none initialized, otherwise returns
     * existing one
     */
    public static Repo getRepo() {
        if (repo == null) {
            repo = new Repo();
            repo.init();
        }

        return repo;
    }

    /**
     * Initializes empty storage maps for all data types and disables importing.
     */
    private void init() {
        data = new HashMap<>();
        for (DataType type : DataType.values()) {
            data.put(type, new TreeMap<>());
        }
        importing = false;
    }

    /**
     * Resets the repository state and clears all stored market data.
     */
    public void reset() {
        init();
    }
}
