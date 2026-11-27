package structures.types.datapoints;

import java.util.Optional;

import com.fasterxml.jackson.databind.node.ObjectNode;

import fileio.MarketDataInput;
import structures.repository.Repo;
import structures.types.enums.DataType;

public final class ImbalancePricePoint extends DataPoint {
    private final Double positiveImbalancePrice;
    private final Double negativeImbalancePrice;
    private static final Double MAX_ABS_PRICE = 15000.0;

    public ImbalancePricePoint(final DataType type,
            final MarketDataInput input,
            final boolean premiumRequired) {
        super(type, input, premiumRequired);
        this.positiveImbalancePrice = input.positiveImbalancePrice();
        this.negativeImbalancePrice = input.negativeImbalancePrice();
    }

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
