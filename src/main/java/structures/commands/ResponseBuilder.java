package structures.commands;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import structures.types.TimeIntervalUtil;
import structures.types.datapoints.DataPoint;
import structures.types.enums.DataType;

public final class ResponseBuilder {

    private ResponseBuilder() {
    }

    /**
     * Builds a WARNING JSON object for an invalid point during import or patch.
     */
    public static ObjectNode buildIngestionWarning(final String commandName,
            final LocalDateTime timestamp, final DataPoint point,
            final String message, final ObjectMapper mapper) {
        ObjectNode node = mapper.createObjectNode();
        node.put("command", commandName);
        node.put("timestamp", TimeIntervalUtil.formatTimestamp(timestamp));
        node.put("status", "WARNING");
        node.put("id", point.getType().name());
        node.put("date", point.getDate().toString());
        node.put("interval", point.getInterval());
        node.put("intervalIndex", point.getIntervalIndex());
        node.put("message", message);
        return node;
    }

    /**
     * Builds a SUCCESS JSON object for a get command with data grouped by date.
     */
    public static ObjectNode buildGetSuccess(final LocalDateTime timestamp,
            final LocalDateTime from, final LocalDateTime to,
            final DataType dataType, final TreeMap<LocalDate, List<DataPoint>> groupedData,
            final ObjectMapper mapper) {
        ObjectNode node = buildGetBase(timestamp, "SUCCESS", from, to, mapper);
        ArrayNode outerData = mapper.createArrayNode();

        for (Map.Entry<LocalDate, List<DataPoint>> entry : groupedData.entrySet()) {
            ObjectNode dayNode = mapper.createObjectNode();
            dayNode.put("id", dataType.name());
            dayNode.put("date", entry.getKey().toString());

            ArrayNode innerData = mapper.createArrayNode();
            for (DataPoint point : entry.getValue()) {
                innerData.add(point.toJson(mapper));
            }
            dayNode.set("data", innerData);
            outerData.add(dayNode);
        }

        node.set("data", outerData);
        return node;
    }

    /**
     * Builds a WARNING JSON object for a get command when no data is available.
     */
    public static ObjectNode buildGetWarning(final LocalDateTime timestamp,
            final LocalDateTime from, final LocalDateTime to,
            final String message, final ObjectMapper mapper) {
        ObjectNode node = buildGetBase(timestamp, "WARNING", from, to, mapper);
        node.putNull("data");
        node.put("message", message);
        return node;
    }

    /**
     * Builds an ERROR JSON object for an unauthorized get command (omits the data
     * field).
     */
    public static ObjectNode buildGetError(final LocalDateTime timestamp,
            final LocalDateTime from, final LocalDateTime to,
            final String message, final ObjectMapper mapper) {
        ObjectNode node = buildGetBase(timestamp, "ERROR", from, to, mapper);
        node.put("message", message);
        return node;
    }

    /**
     * Creates the shared header fields for all get command responses.
     */
    private static ObjectNode buildGetBase(final LocalDateTime timestamp,
            final String status, final LocalDateTime from,
            final LocalDateTime to, final ObjectMapper mapper) {
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "get");
        node.put("timestamp", TimeIntervalUtil.formatTimestamp(timestamp));
        node.put("status", status);
        node.put("from", TimeIntervalUtil.formatTimestamp(from));
        node.put("to", TimeIntervalUtil.formatTimestamp(to));
        return node;
    }
}
