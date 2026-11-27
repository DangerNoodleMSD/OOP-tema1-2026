package structures.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import fileio.CommandInput;
import structures.repository.Repo;
import structures.types.SimulationClock;

public final class PatchCommand extends AbstractIngestionCommand {

    /**
     * Constructs a PatchCommand from raw command input.
     */
    public PatchCommand(final CommandInput cmd) {
        super("patch", cmd);
    }

    @Override
    public void execute(final SimulationClock clock, final Repo repo, final ArrayNode output,
            final ObjectMapper mapper) {
        clock.advanceTo(getTimestamp());
        ingestPoints(repo, output, mapper);
    }
}
