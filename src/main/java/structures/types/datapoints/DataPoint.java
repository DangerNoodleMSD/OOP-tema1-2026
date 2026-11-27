package structures.types.datapoints;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fileio.MarketDataInput;
import structures.repository.Repo;
import structures.types.TimeIntervalUtil;
import structures.types.enums.DataType;

public abstract class DataPoint {
    private final LocalDate date;
    private final String interval;
    private final int intervalIndex;
    private final DataType type;

    protected DataPoint(final DataType type, final MarketDataInput input) {
        this.type = type;
        this.date = LocalDate.parse(input.date());
        this.interval = TimeIntervalUtil.normalizeInterval(input.interval());
        this.intervalIndex = TimeIntervalUtil.computeIntervalIndex(this.interval);
    }

    /**
     * Validates this data point's fields, returning a warning message if invalid or
     * empty if valid.
     */
    public abstract Optional<String> validate(Repo repo);

    /**
     * Converts this data point into a JSON object containing its interval,
     * interval index, and type-specific measurement fields.
     */
    public final ObjectNode toJson(final ObjectMapper mapper) {
        ObjectNode node = mapper.createObjectNode();
        node.put("interval", interval);
        node.put("intervalIndex", intervalIndex);
        addSpecificFields(node);
        return node;
    }

    /**
     * Adds the measurement fields specific to this data point type to the
     * given JSON object node.
     */
    protected abstract void addSpecificFields(ObjectNode node);

    /**
     * Passes this point's grid load quantity to the consumer, if applicable.
     */
    public void acceptGridLoadQuantity(final Consumer<BigDecimal> consumer) {
    }

    public final LocalDate getDate() {
        return date;
    }

    public final int getIntervalIndex() {
        return intervalIndex;
    }

    public final DataType getType() {
        return type;
    }

    public final String getInterval() {
        return interval;
    }
}
