package structures.types.datapoints;

import java.util.Optional;

import com.fasterxml.jackson.databind.node.ObjectNode;

import structures.repository.Repo;

public final class ImbalanceVolPoint extends DataPoint {
    private Double positiveImbalanceVol;
    private Double negativeImbalanceVol;

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
