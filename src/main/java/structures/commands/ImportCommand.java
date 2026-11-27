package structures.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import fileio.CommandInput;
import structures.repository.Repo;
import structures.types.SimulationClock;
import structures.types.datapoints.DataPoint;

public final class ImportCommand extends AbstractIngestionCommand {

    /**
     * Constructs an ImportCommand from raw command input.
     */
    public ImportCommand(final CommandInput cmd) {
        super("import", cmd);
    }

    @Override
    public void execute(final SimulationClock clock, final Repo repo, final ArrayNode output,
            final ObjectMapper mapper) {
        clock.advanceTo(getTimestamp());
        if (!clock.isIngestionAllowed()) {
            return;
        }
        ingestPoints(repo, output, mapper);
    }

    @Override
    protected void onValidationFailure(final Repo repo, final DataPoint point) {
        repo.clearSlot(point.getType(), point.getDate(), point.getIntervalIndex());
    }
}
