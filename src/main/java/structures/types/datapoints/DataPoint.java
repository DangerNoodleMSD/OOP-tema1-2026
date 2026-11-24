package structures.types.datapoints;

import java.time.LocalDate;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import structures.repository.Repo;
import structures.types.enums.DataType;

public abstract class DataPoint {
    private DataType type;
    private LocalDate date;
    private String interval;
    private Integer intervalIndex;

    /**
     *
     * @param repo
     * @return
     */
    public abstract Optional<String> validate(Repo repo);

    public final ObjectNode toJson(final ObjectMapper mapper);

    protected abstract void addSpecificFields(ObjectNode node);
}
