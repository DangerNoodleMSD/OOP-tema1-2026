package structures.repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import structures.types.DailyRepo;
import structures.types.SimulationClock;
import structures.types.enums.DataType;

public final class Repo {
    private static Repo repo = null;
    private SimulationClock simClk;

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
    public void init() {
        simClk = new SimulationClock();
        simClk.reset();
        data = new HashMap<>();
        for (DataType type : DataType.values()) {
            data.put(type, new TreeMap<>());
        }
    }

    public SimulationClock getSimClk() {
        return simClk;
    }
}
