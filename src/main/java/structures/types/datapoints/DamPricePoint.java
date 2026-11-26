package structures.types.datapoints;

import java.util.Optional;

import com.fasterxml.jackson.databind.node.ObjectNode;

import structures.repository.Repo;

public final class DamPricePoint extends DataPoint {
    private Double priceAmount;
    private static final Double MIN_PRICE = -500.0;
    private static final Double MAX_PRICE = 4000.0;

    @Override
    public Optional<String> validate(final Repo repo) {
        if (priceAmount == null) {
            return Optional.of("PriceAmount is null; interval skipped.");
        }

        if (priceAmount < MIN_PRICE || priceAmount > MAX_PRICE) {
            return Optional.of("PriceAmount " + priceAmount
                    + " is outside the market range [-500, 4000] EUR/MWh; interval skipped.");
        }
        return Optional.empty();
    }

    @Override
    protected void addSpecificFields(final ObjectNode node) {
        node.put("priceAmount", priceAmount);
    }
}
