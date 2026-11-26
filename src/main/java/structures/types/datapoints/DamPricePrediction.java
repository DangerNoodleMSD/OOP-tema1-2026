package structures.types.datapoints;

import java.util.Optional;

import com.fasterxml.jackson.databind.node.ObjectNode;

import structures.repository.Repo;

public final class DamPricePrediction extends DataPoint {
    private Double predictedPriceEurMwh;
    private Double spikeProbability;
    private static final Double MIN_PRICE = -500.0;
    private static final Double MAX_PRICE = 4000.0;
    private static final Double MIN_PROB = 0.0;
    private static final Double MAX_PROB = 1.0;

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
