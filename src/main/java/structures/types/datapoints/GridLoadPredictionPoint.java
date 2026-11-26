package structures.types.datapoints;

import java.math.BigDecimal;
import java.util.Optional;

import com.fasterxml.jackson.databind.node.ObjectNode;

import structures.repository.Repo;

public final class GridLoadPredictionPoint extends DataPoint {
    private BigDecimal quantity;
    private Double reservesRequiredMW;

    @Override
    public Optional<String> validate(final Repo repo) {
        if (quantity == null) {
            return Optional.of("Quantity is null; interval skipped.");
        }

        if (quantity.compareTo(BigDecimal.ZERO) < 0) {
            return Optional.of("Quantity " + quantity
                    + " is below the minimum allowed value 0; interval skipped.");
        }

        if (reservesRequiredMW == null) {
            return Optional.of("ReservesRequiredMW is null; interval skipped.");
        }

        if (reservesRequiredMW <= 0.0) {
            return Optional.of("ReservesRequiredMW " + reservesRequiredMW
                    + " is not positive; interval skipped.");
        }

        return Optional.empty();
    }

    @Override
    protected void addSpecificFields(final ObjectNode node) {
        node.put("quantity", quantity);
    }
}
