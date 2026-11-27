package structures.types.datapoints;

import java.util.Optional;

import com.fasterxml.jackson.databind.node.ObjectNode;

import fileio.MarketDataInput;
import structures.repository.Repo;
import structures.types.enums.DataType;

public final class DamPricePrediction extends DataPoint {
    private final Double predictedPriceEurMwh;
    private final Double spikeProbability;
    private static final Double MIN_PRICE = -500.0;
    private static final Double MAX_PRICE = 4000.0;
    private static final Double MIN_PROB = 0.0;
    private static final Double MAX_PROB = 1.0;

    public DamPricePrediction(final DataType type,
            final MarketDataInput input,
            final boolean premiumRequired) {
        super(type, input, premiumRequired);
        this.predictedPriceEurMwh = input.predictedPriceEurMwh();
        this.spikeProbability = input.spikeProbability();
    }

    @Override
    public Optional<String> validate(final Repo repo) {
        if (predictedPriceEurMwh == null) {
            return Optional.of("PredictedPriceEurMwh is null; interval skipped.");
        }

        if (predictedPriceEurMwh < MIN_PRICE || predictedPriceEurMwh > MAX_PRICE) {
            return Optional.of("PredictedPriceEurMwh " + predictedPriceEurMwh
                    + " is outside the market range [-500, 4000] EUR/MWh; interval skipped.");
        }

        if (spikeProbability == null) {
            return Optional.of("SpikeProbability is null; interval skipped.");
        }

        if (spikeProbability < MIN_PROB || spikeProbability > MAX_PROB) {
            return Optional.of("SpikeProbability " + spikeProbability
                    + " is outside the valid range [0.0, 1.0]; interval skipped.");
        }
        return Optional.empty();
    }

    @Override
    protected void addSpecificFields(final ObjectNode node) {
        node.put("predictedPriceEurMwh", predictedPriceEurMwh);
        node.put("spikeProbability", spikeProbability);
    }
}
