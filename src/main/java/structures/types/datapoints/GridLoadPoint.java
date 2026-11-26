package structures.types.datapoints;

import java.math.BigDecimal;
import java.util.Optional;

import com.fasterxml.jackson.databind.node.ObjectNode;

import structures.repository.Repo;

public final class GridLoadPoint extends DataPoint {
    private BigDecimal quantity;

    @Override
    public Optional<String> validate(final Repo repo) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    protected void addSpecificFields(final ObjectNode node) {
        node.put("quantity", quantity);
    }
}
