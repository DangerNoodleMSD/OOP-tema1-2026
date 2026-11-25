package structures.types;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Tracks simulated time in 1-minute ticks and gates automatic import
 * ingestion at 15-tick (:00, :15, :30, :45) boundaries.
 */
public final class SimulationClock {
    private static final int TICKS_PER_INTERVAL = 15;

    private LocalDateTime currentTimestamp;
    private long totalTicks;
    private long lastElapsedTicks;
    private boolean importActive;

    /**
     * Constructs a SimulationClock with importActive initially false.
     */
    public SimulationClock() {
    }

    /**
     * Advances the clock tonextTimestamp, accumulating elapsed 1-minute ticks since
     * the previous timestamp.
     *
     * @param nextTimestamp target simulation timestamp
     */
    public void advanceTo(final LocalDateTime nextTimestamp) {
        if (nextTimestamp == null) {
            throw new NullPointerException("Timestamp must not be null");
        }
        if (currentTimestamp != null) {
            if (nextTimestamp.isBefore(currentTimestamp)) {
                throw new IllegalArgumentException(
                        "Simulation clock cannot move backward: " + currentTimestamp
                                + " -> " + nextTimestamp);
            }
            long elapsed = Duration.between(currentTimestamp, nextTimestamp).toMinutes();
            lastElapsedTicks = elapsed;
            totalTicks += elapsed;
        } else {
            lastElapsedTicks = 0L;
        }
        currentTimestamp = nextTimestamp;
    }

    /**
     * Advances the clock to timestamp and activates automatic import ingestion.
     *
     * @param timestamp command timestamp
     */
    public void startImport(final LocalDateTime timestamp) {
        advanceTo(timestamp);
        importActive = true;
    }

    /**
     * Advances the clock to timestamp and deactivates automatic import ingestion.
     *
     * @param timestamp command timestamp
     */
    public void stopImport(final LocalDateTime timestamp) {
        advanceTo(timestamp);
        importActive = false;
    }

    /**
     * Indicates whether automatic import ingestion is currently permitted by the
     * importActive flag and the 15-tick interval boundary.
     *
     * @return true if import is active and at a 15-tick boundary, false otherwise
     */
    public boolean isIngestionAllowed() {
        return importActive
                && currentTimestamp != null
                && currentTimestamp.getMinute() % TICKS_PER_INTERVAL == 0;
    }

    /**
     * Resets the clock back to its initial state for a fresh simulation run.
     */
    public void reset() {
        currentTimestamp = null;
        totalTicks = 0L;
        lastElapsedTicks = 0L;
        importActive = false;
    }

    public LocalDateTime getCurrentTimestamp() {
        return currentTimestamp;
    }

    public long getTotalTicks() {
        return totalTicks;
    }

    public long getLastElapsedTicks() {
        return lastElapsedTicks;
    }

    public boolean isImportActive() {
        return importActive;
    }
}
