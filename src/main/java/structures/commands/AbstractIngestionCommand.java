package structures.commands;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import fileio.CommandInput;
import fileio.CommandParametersInput;
import fileio.MarketDataInput;
import structures.repository.Repo;
import structures.types.datapoints.DataPoint;
import structures.types.enums.DataType;

/**
 * Base class sharing point validation, storage, and warning emission between
 * import and patch commands.
 */
abstract class AbstractIngestionCommand implements Command {
    private final String commandName;
    private final LocalDateTime timestamp;
    private final DataType defaultDataType;
    private final List<MarketDataInput> rawPoints;

    protected AbstractIngestionCommand(final String commandName, final CommandInput cmd) {
        this.commandName = commandName;
        this.timestamp = LocalDateTime.parse(cmd.getTimestamp());
        CommandParametersInput params = cmd.getParameters();
        if (params != null && params.getDataType() != null) {
            this.defaultDataType = DataType.valueOf(params.getDataType());
        } else {
            this.defaultDataType = null;
        }
        if (params != null && params.getData() != null) {
            this.rawPoints = List.copyOf(params.getData());
        } else {
            this.rawPoints = List.of();
        }
    }

    /**
     * Validates each point in order, saving valid points and emitting warnings for invalid ones.
     */
    protected final void ingestPoints(final Repo repo, final ArrayNode output,
            final ObjectMapper mapper) {
        for (MarketDataInput raw : rawPoints) {
            DataType type = raw.id() != null ? DataType.valueOf(raw.id()) : defaultDataType;
            DataPoint point = type.createPoint(raw);
            Optional<String> error = point.validate(repo);
            if (error.isPresent()) {
                onValidationFailure(repo, point);
                output.add(ResponseBuilder.buildIngestionWarning(
                        commandName, timestamp, point, error.get(), mapper));
            } else {
                repo.save(point);
            }
        }
    }

    /**
     * Hook invoked when a point fails validation (overridden by ImportCommand to clear the slot).
     */
    protected void onValidationFailure(final Repo repo, final DataPoint point) {
    }

    protected final LocalDateTime getTimestamp() {
        return timestamp;
    }
}
