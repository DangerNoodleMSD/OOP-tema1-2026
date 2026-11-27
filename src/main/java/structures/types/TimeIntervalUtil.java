package structures.types;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility methods for normalizing 15-minute interval strings, computing 1-based
 * interval indices (1..96), and converting slots to start/end timestamps.
 */
public final class TimeIntervalUtil {
    private static final int MINUTES_PER_HOUR = 60;
    private static final int INTERVAL_MINUTES = 15;
    private static final int MIN_INTERVAL_INDEX = 1;
    private static final int MAX_INTERVAL_INDEX = 96;
    private static final int EXPECTED_PARTS = 2;

    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    private TimeIntervalUtil() {
    }

    /**
     * Normalizes an interval string such as "00:00-00:15" or "00:00 - 00:15"
     * to the canonical "HH:mm - HH:mm" format.
     */
    public static String normalizeInterval(final String rawInterval) {
        LocalTime[] bounds = parseBounds(rawInterval);
        return bounds[0].format(TIME_FORMATTER) + " - " + bounds[1].format(TIME_FORMATTER);
    }

    /**
     * Computes the 1-based interval index (1..96) for a 15-minute interval string.
     */
    public static int computeIntervalIndex(final String rawInterval) {
        LocalTime start = parseBounds(rawInterval)[0];
        int totalMinutes = start.getHour() * MINUTES_PER_HOUR + start.getMinute();
        return totalMinutes / INTERVAL_MINUTES + 1;
    }

    /**
     * Returns the start LocalDateTime of the given 1-based interval index on date.
     */
    public static LocalDateTime intervalStart(final LocalDate date, final int intervalIndex) {
        validateIntervalIndex(intervalIndex);
        long offsetMinutes = (long) (intervalIndex - 1) * INTERVAL_MINUTES;
        return date.atStartOfDay().plusMinutes(offsetMinutes);
    }

    /**
     * Returns the end LocalDateTime of the given 1-based interval index on date
     * (rolls over to next day 00:00 for intervalIndex 96).
     */
    public static LocalDateTime intervalEnd(final LocalDate date, final int intervalIndex) {
        return intervalStart(date, intervalIndex).plusMinutes(INTERVAL_MINUTES);
    }

    /**
     * Formats a LocalDateTime using "yyyy-MM-dd'T'HH:mm".
     */
    public static String formatTimestamp(final LocalDateTime timestamp) {
        return timestamp.format(TIMESTAMP_FORMATTER);
    }

    /**
     * Validates that an interval index is within [1, 96].
     */
    public static void validateIntervalIndex(final int intervalIndex) {
        if (intervalIndex < MIN_INTERVAL_INDEX || intervalIndex > MAX_INTERVAL_INDEX) {
            throw new IllegalArgumentException(
                    "Interval index out of bounds [1, 96]: " + intervalIndex);
        }
    }

    /**
     * Splits and parses the start and end LocalTime endpoints of a 15-minute interval.
     */
    private static LocalTime[] parseBounds(final String rawInterval) {
        String[] parts = rawInterval.split("-");
        if (parts.length != EXPECTED_PARTS) {
            throw new IllegalArgumentException("Invalid interval format: " + rawInterval);
        }
        LocalTime start = LocalTime.parse(parts[0].trim(), TIME_FORMATTER);
        LocalTime end = LocalTime.parse(parts[1].trim(), TIME_FORMATTER);
        if (start.getMinute() % INTERVAL_MINUTES != 0
                || !start.plusMinutes(INTERVAL_MINUTES).equals(end)) {
            throw new IllegalArgumentException("Invalid 15-minute interval: " + rawInterval);
        }
        return new LocalTime[] {start, end};
    }
}
