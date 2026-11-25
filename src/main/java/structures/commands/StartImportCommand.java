package structures.commands;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import fileio.CommandInput;
import structures.repository.Repo;
import structures.types.SimulationClock;

public final class StartImportCommand implements Command {
    private final LocalDateTime timestamp;

    /**
     * Constructs a StartImportCommand from raw command input.
     *
     * @param cmd raw command input
     */
    public StartImportCommand(final CommandInput cmd) {
        this.timestamp = LocalDateTime.parse(cmd.getTimestamp());
    }

    @Override
    public void execute(final SimulationClock clock,
            final Repo repo,
            final ArrayNode output,
            final ObjectMapper mapper) {
        clock.startImport(timestamp);
    }
}
