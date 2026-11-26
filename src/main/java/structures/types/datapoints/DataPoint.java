package structures.types.datapoints;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import structures.repository.Repo;

public abstract class DataPoint {
    private LocalDate date;
    private String interval;
    private Integer intervalIndex;

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
}
