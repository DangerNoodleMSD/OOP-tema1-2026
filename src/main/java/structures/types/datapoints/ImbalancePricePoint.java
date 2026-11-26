package structures.types.datapoints;

import java.util.Optional;

import com.fasterxml.jackson.databind.node.ObjectNode;

import structures.repository.Repo;

public final class ImbalancePricePoint extends DataPoint {
    private Double positiveImbalancePrice;
    private Double negativeImbalancePrice;
    private static final Double MAX_ABS_PRICE = 15000.0;

    @Override
    public Optional<String> validate(final Repo repo) {
        if (positiveImbalancePrice == null) {
            return Optional.of("PositiveImbalancePrice is null; interval skipped.");
        }

        if (Math.abs(positiveImbalancePrice) > MAX_ABS_PRICE) {
            return Optional.of("PositiveImbalancePrice " + positiveImbalancePrice
                    + " exceeds the maximum allowed value 15000 EUR/MWh; interval skipped.");
        }

        if (negativeImbalancePrice == null) {
            return Optional.of("NegativeImbalancePrice is null; interval skipped.");
        }

        if (Math.abs(negativeImbalancePrice) > MAX_ABS_PRICE) {
            return Optional.of("NegativeImbalancePrice " + negativeImbalancePrice
                    + " exceeds the maximum allowed value 15000 EUR/MWh; interval skipped.");
        }

        return Optional.empty();
    }

    @Override
    protected void addSpecificFields(final ObjectNode node) {
        node.put("positiveImbalancePrice", positiveImbalancePrice);
        node.put("negativeImbalancePrice", negativeImbalancePrice);
    }
}
