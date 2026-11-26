package structures.types.datapoints;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.node.ObjectNode;

import structures.repository.Repo;

public final class GridLoadPoint extends DataPoint {
    private BigDecimal quantity;
    private static final BigDecimal MAX_VARIATION = new BigDecimal("10000");

    @Override
    public Optional<String> validate(final Repo repo) {
        if (quantity == null) {
            return Optional.of("Quantity is null; interval skipped.");
        }

        if (quantity.compareTo(BigDecimal.ZERO) < 0) {
            return Optional.of("Quantity " + quantity
                    + " is below the minimum allowed value 0; interval skipped.");
        }

        Optional<BigDecimal> prev = repo.findPreviousGridLoadQuantity(
                getDate(), getIntervalIndex());
        if (prev.isPresent()
                && quantity.subtract(prev.get()).abs().compareTo(MAX_VARIATION) > 0) {
            return Optional.of("Quantity change exceeds 10000 MW compared with the previous"
                    + " interval; interval skipped.");
        }
        return Optional.empty();
    }

    @Override
    protected void addSpecificFields(final ObjectNode node) {
        node.put("quantity", quantity);
    }

    @Override
    public void acceptGridLoadQuantity(final Consumer<BigDecimal> consumer) {
        consumer.accept(quantity);
    }
}
