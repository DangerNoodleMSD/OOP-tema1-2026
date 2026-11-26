package structures.types.datapoints;

import java.util.Optional;

import com.fasterxml.jackson.databind.node.ObjectNode;

import structures.repository.Repo;

public final class ImbalanceVolPoint extends DataPoint {
    private Double positiveImbalanceVol;
    private Double negativeImbalanceVol;

    @Override
    public Optional<String> validate(final Repo repo) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    protected void addSpecificFields(final ObjectNode node) {
        node.put("positiveImbalanceVol", positiveImbalanceVol);
        node.put("negativeImbalanceVol", negativeImbalanceVol);
    }
}
