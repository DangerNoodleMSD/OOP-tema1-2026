package structures.types.datapoints;

import java.util.Optional;

import com.fasterxml.jackson.databind.node.ObjectNode;

import structures.repository.Repo;

public final class ImbalancePricePoint extends DataPoint {
    private Double positiveImbalancePrice;
    private Double negativeImbalancePrice;

    @Override
    public Optional<String> validate(final Repo repo) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    protected void addSpecificFields(final ObjectNode node) {
        node.put("positiveImbalancePrice", positiveImbalancePrice);
        node.put("negativeImbalancePrice", negativeImbalancePrice);
    }
}
