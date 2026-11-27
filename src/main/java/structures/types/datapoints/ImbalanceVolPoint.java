package structures.types.datapoints;

import java.util.Optional;

import com.fasterxml.jackson.databind.node.ObjectNode;

import fileio.MarketDataInput;
import structures.repository.Repo;
import structures.types.enums.DataType;

public final class ImbalanceVolPoint extends DataPoint {
    private final Double positiveImbalanceVol;
    private final Double negativeImbalanceVol;

    public ImbalanceVolPoint(final DataType type,
            final MarketDataInput input,
            final boolean premiumRequired) {
        super(type, input, premiumRequired);
        this.positiveImbalanceVol = input.positiveImbalanceVol();
        this.negativeImbalanceVol = input.negativeImbalanceVol();
    }

    @Override
    public Optional<String> validate(final Repo repo) {
        if (positiveImbalanceVol == null) {
            return Optional.of("PositiveImbalanceVol is null; interval skipped.");
        }

        if (positiveImbalanceVol < 0.0) {
            return Optional.of("PositiveImbalanceVol " + positiveImbalanceVol
                    + " is below the minimum allowed value 0; interval skipped.");
        }

        if (negativeImbalanceVol == null) {
            return Optional.of("NegativeImbalanceVol is null; interval skipped.");
        }

        if (negativeImbalanceVol < 0.0) {
            return Optional.of("NegativeImbalanceVol " + negativeImbalanceVol
                    + " is below the minimum allowed value 0; interval skipped.");
        }
        return Optional.empty();
    }

    @Override
    protected void addSpecificFields(final ObjectNode node) {
        node.put("positiveImbalanceVol", positiveImbalanceVol);
        node.put("negativeImbalanceVol", negativeImbalanceVol);
    }
}
