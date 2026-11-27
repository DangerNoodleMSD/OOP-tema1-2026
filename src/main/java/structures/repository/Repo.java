package structures.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import structures.templateio.TemplateLoader;
import structures.types.DailyRepo;
import structures.types.SimulationClock;
import structures.types.datapoints.DataPoint;
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

    /**
     * Walks backwards through GRID_LOAD daily repos from the slot strictly before
     * (date, intervalIndex) and returns the first valid quantity found.
     */
    public Optional<BigDecimal> findPreviousGridLoadQuantity(final LocalDate date,
            final int intervalIndex) {
        TreeMap<LocalDate, DailyRepo> gridDays = data.get(DataType.GRID_LOAD);

        DailyRepo sameDay = gridDays.get(date);
        if (sameDay != null) {
            Optional<DataPoint> earlierToday = sameDay.findLastNonNullBefore(intervalIndex);
            if (earlierToday.isPresent()) {
                return extractGridLoadQuantity(earlierToday.get());
            }
        }

        for (DailyRepo priorDay : gridDays.headMap(date, false).descendingMap().values()) {
            Optional<DataPoint> latestPrior = priorDay.findLastNonNull();
            if (latestPrior.isPresent()) {
                return extractGridLoadQuantity(latestPrior.get());
            }
        }

        return Optional.empty();
    }

    /**
     * Extracts the grid load quantity from a DataPoint polymorphically without
     * instanceof.
     */
    private Optional<BigDecimal> extractGridLoadQuantity(final DataPoint point) {
        List<BigDecimal> box = new ArrayList<>(1);
        point.acceptGridLoadQuantity(box::add);
        if (box.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(box.getFirst());
    }

    void save(final DataPoint point) {
        TreeMap<LocalDate, DailyRepo> byDate = data.get(point.getType());
        DailyRepo day = byDate.computeIfAbsent(point.getDate(),
                d -> new DailyRepo(point.getType(), d, TemplateLoader.load(point.getType())));
        day.put(point);
    }

    void clearSlot(final DataType type, final LocalDate date, final int intervalIndex) {
        DailyRepo day = data.get(type).get(date);
        if (day != null) {
            day.clearSlot(intervalIndex);
        }
    }

    Optional<DataPoint> findLatest(final DataType type) {
        for (DailyRepo day : data.get(type).descendingMap().values()) {
            Optional<DataPoint> point = day.findLastNonNull();
            if (point.isPresent()) {
                return point;
            }
        }
        return Optional.empty();
    }

    TreeMap<LocalDate, List<DataPoint>> findInRange(final DataType type, final LocalDateTime start,
            final LocalDateTime end) {

    }
}
