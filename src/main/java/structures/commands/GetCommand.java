package structures.commands;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import fileio.CommandInput;
import fileio.CommandParametersInput;
import structures.repository.Repo;
import structures.types.SimulationClock;
import structures.types.TimeIntervalUtil;
import structures.types.datapoints.DataPoint;
import structures.types.enums.ClientTier;
import structures.types.enums.DataType;

public final class GetCommand implements Command {
    private static final int INTERVAL_MINUTES = 15;

    private final LocalDateTime timestamp;
    private final ClientTier clientTier;
    private final DataType dataType;
    private final String target;
    private final LocalDateTime start;
    private final LocalDateTime end;

    /**
     * Constructs a GetCommand from raw command input.
     */
    public GetCommand(final CommandInput cmd) {
        this.timestamp = LocalDateTime.parse(cmd.getTimestamp());
        CommandParametersInput params = cmd.getParameters();
        this.clientTier = ClientTier.valueOf(params.getClientTier());
        this.dataType = DataType.valueOf(params.getDataType());
        this.target = params.getTarget();
        this.start = params.getStart() != null ? LocalDateTime.parse(params.getStart()) : null;
        this.end = params.getEnd() != null ? LocalDateTime.parse(params.getEnd()) : null;
    }

    @Override
    public void execute(final SimulationClock clock, final Repo repo, final ArrayNode output,
            final ObjectMapper mapper) {
        clock.advanceTo(timestamp);

        LocalDateTime[] range = resolveRange(repo);
        LocalDateTime from = range[0];
        LocalDateTime to = range[1];

        if (!clientTier.canAccess(dataType)) {
            output.add(ResponseBuilder.buildGetError(timestamp, from, to,
                    "Access denied: " + dataType.name() + " requires PREMIUM subscription.",
                    mapper));
            return;
        }

        TreeMap<LocalDate, List<DataPoint>> grouped = repo.findInRange(dataType, from, to);
        if (grouped.isEmpty()) {
            output.add(ResponseBuilder.buildGetWarning(timestamp, from, to,
                    "Data is not yet available. Try again later.", mapper));
        } else {
            output.add(ResponseBuilder.buildGetSuccess(
                    timestamp, from, to, dataType, grouped, mapper));
        }
    }

    /**
     * Resolves the half-open query window [from, to) for LATEST, exact target, or start/end.
     */
    private LocalDateTime[] resolveRange(final Repo repo) {
        if ("LATEST".equals(target)) {
            Optional<DataPoint> latest = repo.findLatest(dataType);
            if (latest.isPresent()) {
                DataPoint point = latest.get();
                LocalDateTime from = TimeIntervalUtil.intervalStart(
                        point.getDate(), point.getIntervalIndex());
                LocalDateTime to = TimeIntervalUtil.intervalEnd(
                        point.getDate(), point.getIntervalIndex());
                return new LocalDateTime[] {from, to};
            }
            return new LocalDateTime[] {timestamp.minusMinutes(INTERVAL_MINUTES), timestamp};
        }
        if (target != null) {
            LocalDateTime from = LocalDateTime.parse(target);
            return new LocalDateTime[] {from, from.plusMinutes(INTERVAL_MINUTES)};
        }
        return new LocalDateTime[] {start, end};
    }
}
